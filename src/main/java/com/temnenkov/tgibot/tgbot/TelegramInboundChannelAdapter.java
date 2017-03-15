package com.temnenkov.tgibot.tgbot;

import com.google.gson.*;
import com.temnenkov.tgibot.http.TelegramHttpSender;
import com.temnenkov.tgibot.tgapi.dto.Update;
import com.temnenkov.tgibot.tgapi.method.GetUpdates;
import com.temnenkov.tgibot.tgapi.method.TelegramMethod;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class TelegramInboundChannelAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramInboundChannelAdapter.class);
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private TelegramHttpSender httpSender;
    private long lastReceivedUpdate = 0;

    public UpdatePack fetchUpdates() throws IOException {
        LOGGER.debug("try get update {}", lastReceivedUpdate);

        String jsonRequest = getJsonRequest();
        UpdatePack updatePack = parseUpdates(jsonRequest);
        LOGGER.debug("parse updates {}", updatePack);

        if (updatePack != null && !updatePack.isEmpty()){
            lastReceivedUpdate = updatePack.getLastReceivedUpdate();
            return updatePack;
        }

        return null;

    }

    private String getJsonRequest() throws IOException {
        GetUpdates request = new GetUpdates();
        request.setLimit(100);
        request.setOffset(lastReceivedUpdate + 1);
        request.setTimeout(0);

        final String jsonRequest = gson.toJson(request);

        HttpPost httpPost = httpSender.createPost(TelegramMethod.GETUPDATES.name(), jsonRequest);

        String responseContent;
        try (CloseableHttpResponse response = httpSender.getHttpclient().execute(httpPost)) {
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("[Out] {} {}", TelegramMethod.GETUPDATES.name(), jsonRequest);
                LOGGER.debug("[InR] {}", response);
            }
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
            LOGGER.debug("[InC] {}", responseContent);
        }
        return responseContent;
    }

    private UpdatePack parseUpdates(String json) {
        JsonObject jsonObject;
        try{
            jsonObject = gson.fromJson(json, JsonObject.class);

        } catch(JsonSyntaxException e){
            LOGGER.error("Error parse updates {}", json, e);
            return null;
        }

        if (!jsonObject.get("ok").getAsBoolean()) {
            LOGGER.error("Error getting updates {}", json);
            return null;
        }

        final JsonElement result = jsonObject.get("result");
        Update[] updates = gson.fromJson(result, Update[].class);

        Optional<Long> maxUpdate = Arrays.stream(updates)
                .map(Update::getUpdateId)
                .max(Long::compare);
        long last = maxUpdate.isPresent() ? maxUpdate.get() : 0L;
        LOGGER.debug("last reseived update {}", last);
        return new UpdatePack(updates, last);
    }

    @Required
    public void setHttpSender(TelegramHttpSender httpSender) {
        this.httpSender = httpSender;
    }


}
