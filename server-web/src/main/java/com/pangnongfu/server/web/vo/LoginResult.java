package com.pangnongfu.server.web.vo;

/**
 * Author:zhangyu
 * create on 15/9/20.
 */
public class LoginResult{

    public static final String SUCCESS_STATUS="success";
    public static final String FAILED_STATUS="failed";

    private String status;
    private String message;

    private String token;
    private String ossAccesskey;
    private String ossAccessSecret;

    private UserDetailVO userDetail;

    public String getStatus() {
        return status;
    }

    public LoginResult setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public LoginResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LoginResult setToken(String token) {
        this.token = token;
        return this;
    }

    public UserDetailVO getUserDetail() {
        return userDetail;
    }

    public LoginResult setUserDetail(UserDetailVO userDetail) {
        this.userDetail = userDetail;
        return this;
    }

    public String getOssAccesskey() {
        return ossAccesskey;
    }

    public void setOssAccesskey(String ossAccesskey) {
        this.ossAccesskey = ossAccesskey;
    }

    public String getOssAccessSecret() {
        return ossAccessSecret;
    }

    public void setOssAccessSecret(String ossAccessSecret) {
        this.ossAccessSecret = ossAccessSecret;
    }
}
