package com.pangnongfu.server.bal.api;

import com.pangnongfu.server.bal.dto.CommentDTO;
import com.pangnongfu.server.bal.dto.Pageable;
import com.pangnongfu.server.bal.exception.BizException;

/**
 * Created by shuiyu on 2015/10/8.
 */
public interface CommentService {
    /**
     * 获得状态下的评论
     * @param statusId
     * @param page
     * @param count
     * @return
     * @throws BizException
     */
    Pageable<CommentDTO> listCommentByStatus(long statusId,int page,int count)throws BizException;

    /**
     * 添加评论
     *
     *  */
    void addComment(long userId,long statusId,String content)throws BizException;

    /**
     * 回复评论
     * @param userId
     * @param commentId
     * @param content
     * @throws BizException
     */
    void replayComment(long userId,long commentId,String content)throws BizException;

    /**
     * 获取用户发布的评论
     * @param userId
     * @param page
     * @param count
     * @return
     * @throws BizException
     */
    Pageable<CommentDTO> listMyComment(long userId,int page,int count)throws BizException;

    /**
     * 删除评论
     * @param commentId
     * @throws BizException
     */
    void deleteComment(long commentId)throws BizException;
}
