package com.pangnongfu.server.bal.dto;

/**
 * Author:zhangyu
 * create on 15/9/24.
 */
public class StatusImageDTO {

    public static final String high_quality="high";
    public static final String normal_quality="normal";
    public static final String low_quality="low";

    private String url;
    private String quality;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}
