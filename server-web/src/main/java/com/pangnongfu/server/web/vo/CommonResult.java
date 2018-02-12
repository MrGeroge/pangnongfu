package com.pangnongfu.server.web.vo;

/**
 *
 * 通用返回数据
 *
 * Author:zhangyu
 * create on 15/9/20.
 */
public class CommonResult{

    public static final String SUCCESS_STATUS="success";
    public static final String FAILED_STATUS="failed";

    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public CommonResult setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommonResult setMessage(String message) {
        this.message = message;
        return this;
    }

}
