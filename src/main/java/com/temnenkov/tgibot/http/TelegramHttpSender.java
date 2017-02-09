package com.temnenkov.tgibot.http;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Required;

import java.nio.charset.StandardCharsets;

public class TelegramHttpSender extends BaseHttpSender {

    private String botToken;

    public HttpPost createPost(String method, String body){

        String url = "https://api.telegram.org/bot" + botToken + "/" + method;
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("charset", StandardCharsets.UTF_8.name());
        httpPost.setConfig(getRequestConfig());
        httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));

        return httpPost;
    }

    @Required
    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

}
