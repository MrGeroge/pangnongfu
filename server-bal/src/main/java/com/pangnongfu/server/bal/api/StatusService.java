package com.pangnongfu.server.bal.api;

import com.pangnongfu.server.bal.dto.*;
import com.pangnongfu.server.bal.exception.BizException;

import java.util.List;

/**
 * Author:zhangyu
 * create on 15/9/20.
 */
public interface StatusService {
    /**
     * 获取公共状态
     * @param page 页号
     * @param count 每页记录数量
     * */
    Pageable<StatusDTO> listPublicStatuses(int page,int count)throws BizException;

    /**
     * 得到用户发布的状态
     * @param userId 用户ID
     * @param page 页号
     * @param count 每页记录数量
     * */
    Pageable<StatusDTO> listStatusesByUser(long userId,int page,int count)throws BizException;

    /**
     * 获取相应分类下的状态
     *
     * @param categoryId 状态类别ID
     * @param page 页号
     * @param count 每页记录数量
     * */
    Pageable<StatusDTO> listStatusesByCategory(long categoryId,int page,int count)throws BizException;


    /**
     * 获取用户发布的状态
     *
     * @param userId 用户ID
     * @param page 页号
     * @param count  每页记录数量
     *
     * */
    Pageable<StatusDTO> listCollectedStatuses(long userId,int page,int count)throws BizException;

    /**
     * 添加状态
     *
     * */
    void addStatus(long userId,
                   long categoryId,
                   String text,
                   double longitude,
                   double latitude,
                   String address,
                   List<StatusImageDTO> images) throws BizException;

    /**
     * 删除状态
     *
     * */
    void deleteStatus(long statusId) throws BizException;

    /**
     * 添加评论
     *
     *  */
    @Deprecated
    void addComment(long userId,long statusId,String content)throws BizException;

    /**
     * 获取状态下的评论
     *
     * */
    @Deprecated
    List<CommentDTO> listCommentByStatus(long statusId)throws BizException;

    /**
     * 点赞状态
     *
     * */
    void likeStatus(long userId,long statusId)throws BizException;

    /**
     * 取消点赞
     *
     * */
    void unlikeStatus(long userId,long statusId)throws BizException;

    /***
     * 判断是否点赞
     * @param userId
     * @param statusId
     * @return
     */
    boolean isLikeStatus(long userId,long statusId)throws BizException;

    /**
     * 得到状态所有分类
     *
     * */
    List<StatusCategoryDTO> listAllCategory()throws BizException;

    /**
     * 添加收藏
     * */
    void addCollection(long statusId,long userId)throws BizException;

    /**
     * 取消收藏
     * */
    void removeCollection(long statusId,long userId)throws BizException;

    /**
     * 判断是否收藏
     * */
    boolean isCollected(long statusId , long userId)throws BizException;

    /**
     * 根据id获取单条信息
     *
     * */
    StatusDTO getStatusById(long statusId)throws BizException;

    /**
     * 根据地区获得发布的状态
     * @param city
     * @param district
     * @param page
     * @param count
     * @return
     */
    Pageable<StatusDTO> listStatusByLocation(String city,String district,int page,int count)throws BizException;

    /**
     * 得到用户点赞的状态
     * @param userId
     * @param page
     * @param count
     * @return
     * @throws BizException
     */
    Pageable<StatusDTO> listLikedStatusByUserId(long userId,int page,int count)throws  BizException;

    /**
     * 得到用户收藏的状态
     * @param userId
     * @param page
     * @param count
     * @return
     * @throws BizException
     */
    Pageable<StatusDTO> listCollectedStatusByUserId(long userId,int page,int count)throws BizException;
}
