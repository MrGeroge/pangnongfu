package com.pangnongfu.server.web.vo;

/**
 * Author:zhangyu
 * create on 15/9/20.
 */
public class UserDetailVO {

    private long userId;

    private String nickname;
    private String avatarUrl;
    private String gender;
    private String city;
    private String district;
    private String about;
    private String longitude;
    private String latitude;
    private String address;
    public UserDetailVO(){}

    public UserDetailVO(long userId, String nickname, String avatarUrl, String gender, String city, String district, String about) {
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

    public UserDetailVO setUserId(long userId) {
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
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserDetailVO{" +
                "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", about='" + about + '\'' +
                '}';
    }
}
