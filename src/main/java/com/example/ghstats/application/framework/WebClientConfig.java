package com.example.ghstats.application.framework;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("webclient")
public class WebClientConfig {
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
