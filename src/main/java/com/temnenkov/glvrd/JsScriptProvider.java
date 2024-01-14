package com.temnenkov.glvrd;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class JsScriptProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsScriptProvider.class);

    private final AtomicReference<String> content = new AtomicReference<>();

    private DownloadJsScript downloadJsScript;

    public String getScript(){
        return content.get();
    }

    public void updateScript() throws IOException {
         String newScript = getScriptContent();
         content.set(newScript);
    }

    private String getScriptContent() throws IOException {

        HttpGet request = downloadJsScript.createGet("https://api.glvrd.ru/v3/glvrd.js");

        String responseContent;
        try (CloseableHttpResponse response = downloadJsScript.getHttpclient().execute(request)) {
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            responseContent = EntityUtils.toString(buf, StandardCharsets.UTF_8);
        }
        LOGGER.debug("get script {}", responseContent);
        return responseContent;
    }

    @Required
    public void setDownloadJsScript(DownloadJsScript downloadJsScript) {
        this.downloadJsScript = downloadJsScript;
    }

}
