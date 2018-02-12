package com.pangnongfu.server.dal.po;

import javax.persistence.*;

/**
 * Created by xinghai on 2015/9/20.
 */
@Entity(name="status_image")
@Table(name="tb_status_image")
public class StatusImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="url",nullable = false)
    private String url;
    @Column(name=" quality",nullable = false)
    private int quality;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="status_id")
    private Status status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusImage that = (StatusImage) o;

        if (id != that.id) return false;
        if (quality != that.quality) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        return !(status != null ? !status.equals(that.status) : that.status != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + quality;
        return result;
    }
}
