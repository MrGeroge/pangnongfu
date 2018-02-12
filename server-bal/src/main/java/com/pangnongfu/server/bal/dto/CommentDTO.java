package com.pangnongfu.server.bal.dto;

import java.util.Date;

/**
 * Author:zhangyu
 * create on 15/9/24.
 */
public class CommentDTO {

    private long commentId;

    private AccountSimpleDTO publisher;

    private String content;

    private Date createAt;

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public AccountSimpleDTO getPublisher() {
        return publisher;
    }

    public void setPublisher(AccountSimpleDTO publisher) {
        this.publisher = publisher;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
