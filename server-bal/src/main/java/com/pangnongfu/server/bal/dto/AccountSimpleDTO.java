package com.pangnongfu.server.bal.dto;

/**
 * Author:zhangyu
 * create on 15/9/21.
 */
public class AccountSimpleDTO {

    private long userId;
    private String nickname;
    private String avatarUrl;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
