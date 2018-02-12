package com.pangnongfu.server.bal.dto;

/**
 * Author:zhangyu
 * create on 15/9/24.
 */
public class StatusCategoryDTO {

    private long categoryId;
    private String name;
    private String imageUrl;
    private long statusNum;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getStatusNum(){ return statusNum; }

    public void setStatusNum(long statusNum){ this.statusNum = statusNum;}
}
