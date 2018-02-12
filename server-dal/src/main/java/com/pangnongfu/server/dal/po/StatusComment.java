package com.pangnongfu.server.dal.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xinghai on 2015/9/20.
 */
@Entity(name="status_comment")
@Table(name=" tb_status_comment")
public class StatusComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="publisher_id")
    private Account commentPublisher;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="status_id")
    private Status status;
    @Column(name="create_at",nullable = false)
    private Date create_at;
    @Lob
    @Column(name="content",nullable = false)
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="parent_comment_id")
    private StatusComment parentComment;
    @OneToMany(mappedBy = "parentComment",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @OrderBy("create_at")
    private List<StatusComment> childComment=new ArrayList<StatusComment>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getCommentPublisher() {
        return commentPublisher;
    }

    public void setCommentPublisher(Account commentPublisher) {
        this.commentPublisher = commentPublisher;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public StatusComment getParentComment() {
        return parentComment;
    }

    public void setParentComment(StatusComment parentComment) {
        this.parentComment = parentComment;
    }

    public List<StatusComment> getChildComment() {
        return childComment;
    }

    public void setChildComment(List<StatusComment> childComment) {
        this.childComment = childComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusComment that = (StatusComment) o;

        if (id != that.id) return false;
        if (commentPublisher != null ? !commentPublisher.equals(that.commentPublisher) : that.commentPublisher != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (create_at != null ? !create_at.equals(that.create_at) : that.create_at != null) return false;
        return !(content != null ? !content.equals(that.content) : that.content != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (create_at != null ? create_at.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

}
