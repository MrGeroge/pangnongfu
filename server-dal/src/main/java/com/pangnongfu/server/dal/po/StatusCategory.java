package com.pangnongfu.server.dal.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xinghai on 2015/9/20.
 */
@Entity(name="status_category")
@Table(name="tb_status_category")
public class StatusCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="img_url",nullable = false)
    private String img_url;
    @Column(name="title",nullable = false)
    private String title;
    @Column(name="status_num")
    private long status_num;
    @OneToMany(mappedBy = "statusCategory",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    private Set<Status> listStatus=new HashSet<Status>();
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStatus_num() {
        return status_num;
    }

    public void setStatus_num(long status_num) {
        this.status_num = status_num;
    }

    public Set<Status> getListStatus() {
        return listStatus;
    }

    public void setListStatus(Set<Status> listStatus) {
        this.listStatus = listStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusCategory that = (StatusCategory) o;

        if (id != that.id) return false;
        if (status_num != that.status_num) return false;
        if (img_url != null ? !img_url.equals(that.img_url) : that.img_url != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return !(listStatus != null ? !listStatus.equals(that.listStatus) : that.listStatus != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (img_url != null ? img_url.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (int) (status_num ^ (status_num >>> 32));
        return result;
    }
}
