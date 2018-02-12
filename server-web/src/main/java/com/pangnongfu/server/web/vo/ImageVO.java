package com.pangnongfu.server.web.vo;

/**
 * Created by shuiyu on 2015/9/30.
 */
public class ImageVO {

    public static final String high_quality="high";
    public static final String normal_quality="normal";
    public static final String low_quality="low";

    private String origin_url;
    private String quality;

    public String getOrigin_url() {
        return origin_url;
    }

    public ImageVO setOrigin_url(String url) {
        this.origin_url = origin_url;
        return this;
    }

    public String getQuality() {
        return quality;
    }

    public ImageVO setQuality(String quality) {
        this.quality = quality;
        return this;
    }
}
