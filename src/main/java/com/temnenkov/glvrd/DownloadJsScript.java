package com.temnenkov.glvrd;

import com.temnenkov.tgibot.http.BaseHttpSender;
import org.apache.http.client.methods.HttpGet;

import java.nio.charset.StandardCharsets;

public class DownloadJsScript extends BaseHttpSender {

    public HttpGet createGet(String url) {

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("charset", StandardCharsets.UTF_8.name());
        httpGet.setConfig(getRequestConfig());

        return httpGet;
    }


}
