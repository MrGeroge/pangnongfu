package com.pangnongfu.server.bal.dto;

/**
 * Author:zhangyu
 * create on 15/9/21.
 */
public class AccountDetailDTO {

    public static final String GENDER_MALE="male";
    public static final String GENDER_FEMALE="female";


    private long userId;

    private String nickname;
    private String avatarUrl;
    private String gender;
    private String city;
    private String district;
    private String about;

    public AccountDetailDTO(){}

    public AccountDetailDTO(long userId, String nickname, String avatarUrl, String gender, String city, String district, String about) {
        this.userId = userId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.city = city;
        this.district = district;
        this.about = about;
    }

    public long getUserId() {
        return userId;
    }

    public AccountDetailDTO setUserId(long userId) {
        this.userId = userId;
        return this;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

}
