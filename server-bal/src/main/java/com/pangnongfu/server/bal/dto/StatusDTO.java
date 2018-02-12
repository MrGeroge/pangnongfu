package com.pangnongfu.server.bal.dto;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Author:zhangyu
 * create on 15/9/21.
 */
public class StatusDTO {

    private long statusId;

    private AccountSimpleDTO publisherId;

    private Date createAt;

    private StatusCategoryDTO category;

    private String text;

    private String city;

    private String district;

    private long commentNum;

    private long loveNum;

    private double longitude;

    private double latitude;

    private String address;

    private List<StatusImageDTO> images=new LinkedList<>();

    public String getCity(){return city;}

    public void setCity(String city){this.city=city;}

    public String getDistrict(){return district;}

    public void setDistrict(String district){this.district=district;}

    public long getStatusId() {
        return statusId;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public AccountSimpleDTO getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(AccountSimpleDTO publisherId) {
        this.publisherId = publisherId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public StatusCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(StatusCategoryDTO category) {
        this.category = category;
    }

    public long getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(long commentNum) {
        this.commentNum = commentNum;
    }

    public long getLoveNum() {
        return loveNum;
    }

    public void setLoveNum(long loveNum) {
        this.loveNum = loveNum;
    }

    public List<StatusImageDTO> getImages() {
        return images;
    }

    public void setImages(List<StatusImageDTO> images) {
        this.images = images;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
