package com.pangnongfu.server.dal.po;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by xinghai on 2015/9/20.
 */
@Entity(name="notification")
@Table(name="tb_notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="producer_id")
    private long producer_id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="consumer_id",nullable = false)
    private Account consumer;
    @Column(name="post_status")
    private int post_status;
    @Column(name="create_at",nullable = false)
    private Date create_at;
    @Lob
    @Column(name="content",nullable = false)
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProducer_id() {
        return producer_id;
    }

    public void setProducer_id(long producer_id) {
        this.producer_id = producer_id;
    }

    public Account getConsumer() {
        return consumer;
    }

    public void setConsumer(Account consumer) {
        this.consumer = consumer;
    }

    public int getPost_status() {
        return post_status;
    }

    public void setPost_status(int post_status) {
        this.post_status = post_status;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (id != that.id) return false;
        if (producer_id != that.producer_id) return false;
        if (post_status != that.post_status) return false;
        if (consumer != null ? !consumer.equals(that.consumer) : that.consumer != null) return false;
        if (create_at != null ? !create_at.equals(that.create_at) : that.create_at != null) return false;
        return !(content != null ? !content.equals(that.content) : that.content != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (producer_id ^ (producer_id >>> 32));
        result = 31 * result + post_status;
        result = 31 * result + (create_at != null ? create_at.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

}
