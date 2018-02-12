package com.pangnongfu.server.dal.po;

import javax.persistence.*;
import java.util.*;

/**
 * Created by xinghai on 2015/9/20.
 */
@Entity(name="status")
@Table(name="tb_status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="created_at",nullable = false)
    private Date created_at;
    @Column(name="text",nullable = false)
    private String text;
    @Column(name="comment_num")
    private long comment_num;
    @Column(name="love_num")
    private long love_num;
    @Column(name="longitude",nullable = false)
    private double longitude;
    @Column(name="latitude",nullable = false)
    private double latitude;
    @Column(name="address",nullable = false)
    private String address;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="publisher_id")
    private Account statusPublisher;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="category_id")
    private StatusCategory statusCategory;
    @OneToMany(mappedBy = "status",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private Set<StatusImage> listStatusImage=new HashSet<StatusImage>();
    @OneToMany(mappedBy = "status",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private Set<StatusCollect> listStatusCollect=new HashSet<StatusCollect>();
    @OneToMany(mappedBy = "status",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private Set<StatusLove> listStatusLove=new HashSet<StatusLove>();
    @OneToMany(mappedBy = "status",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private Set<StatusComment> listStatusComment=new HashSet<StatusComment>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getComment_num() {
        return comment_num;
    }

    public void setComment_num(long comment_num) {
        this.comment_num = comment_num;
    }

    public long getLove_num() {
        return love_num;
    }

    public void setLove_num(long love_num) {
        this.love_num = love_num;
    }

    public Account getStatusPublisher() {
        return statusPublisher;
    }

    public void setStatusPublisher(Account statusPublisher) {
        this.statusPublisher = statusPublisher;
    }

    public Set<StatusImage> getListStatusImage() {
        return listStatusImage;
    }

    public void setListStatusImage(Set<StatusImage> listStatusImage) {
        this.listStatusImage = listStatusImage;
    }

    public Set<StatusCollect> getListStatusCollect() {
        return listStatusCollect;
    }

    public void setListStatusCollect(Set<StatusCollect> listStatusCollect) {
        this.listStatusCollect = listStatusCollect;
    }

    public Set<StatusLove> getListStatusLove() {
        return listStatusLove;
    }

    public void setListStatusLove(Set<StatusLove> listStatusLove) {
        this.listStatusLove = listStatusLove;
    }

    public Set<StatusComment> getListStatusComment() {
        return listStatusComment;
    }

    public void setListStatusComment(Set<StatusComment> listStatusComment) {
        this.listStatusComment = listStatusComment;
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

    public StatusCategory getStatusCategory() {
        return statusCategory;
    }

    public void setStatusCategory(StatusCategory statusCategory) {
        this.statusCategory = statusCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Status status = (Status) o;

        if (id != status.id) return false;
        if (comment_num != status.comment_num) return false;
        if (love_num != status.love_num) return false;
        if (Double.compare(status.longitude, longitude) != 0) return false;
        if (Double.compare(status.latitude, latitude) != 0) return false;
        if (created_at != null ? !created_at.equals(status.created_at) : status.created_at != null) return false;
        if (text != null ? !text.equals(status.text) : status.text != null) return false;
        if (address != null ? !address.equals(status.address) : status.address != null) return false;
        if (statusPublisher != null ? !statusPublisher.equals(status.statusPublisher) : status.statusPublisher != null)
            return false;
        if (statusCategory != null ? !statusCategory.equals(status.statusCategory) : status.statusCategory != null)
            return false;
        if (listStatusImage != null ? !listStatusImage.equals(status.listStatusImage) : status.listStatusImage != null)
            return false;
        if (listStatusCollect != null ? !listStatusCollect.equals(status.listStatusCollect) : status.listStatusCollect != null)
            return false;
        if (listStatusLove != null ? !listStatusLove.equals(status.listStatusLove) : status.listStatusLove != null)
            return false;
        return !(listStatusComment != null ? !listStatusComment.equals(status.listStatusComment) : status.listStatusComment != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (created_at != null ? created_at.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (int) (comment_num ^ (comment_num >>> 32));
        result = 31 * result + (int) (love_num ^ (love_num >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}
