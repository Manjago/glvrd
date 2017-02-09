package com.temnenkov.tgibot.http;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public abstract class BaseHttpSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseHttpSender.class);

    private static final int SOCKET_TIMEOUT = 30 * 1000;
    private CloseableHttpClient httpclient;
    private RequestConfig requestConfig;

    private String proxy;
    private int port;
    private boolean debugSSL;

    public CloseableHttpClient getHttpclient() {
        return httpclient;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    protected void initMethod() throws NoSuchAlgorithmException, KeyManagementException {

        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setConnectionTimeToLive(10, TimeUnit.SECONDS)
                .setMaxConnTotal(10);


        if (port !=0 ){
            httpClientBuilder
                    .setProxy(new HttpHost(proxy, port));
        }

        if (debugSSL){
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        @Override
                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        @Override
                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };


            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sc);
            httpClientBuilder.setSSLSocketFactory(sslsf);
        }

        httpclient = httpClientBuilder.build();

        requestConfig = RequestConfig.copy(RequestConfig.custom().build())
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(SOCKET_TIMEOUT)
                .setConnectionRequestTimeout(SOCKET_TIMEOUT).build();
    }

    protected void destroyMethod() {
        if (httpclient != null) {
            try {
                httpclient.close();
            } catch (IOException e) {
                LOGGER.error("fail close httpclient", e);
            }
            httpclient = null;
        }
    }

    @Required
    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    @Required
    public void setPort(int port) {
        this.port = port;
    }

    @Required
    public void setDebugSSL(boolean debugSSL) {
        this.debugSSL = debugSSL;
    }


}
