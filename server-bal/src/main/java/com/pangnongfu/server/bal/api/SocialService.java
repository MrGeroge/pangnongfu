package com.pangnongfu.server.bal.api;

import com.pangnongfu.server.bal.dto.AccountSimpleDTO;
import com.pangnongfu.server.bal.dto.Pageable;
import com.pangnongfu.server.bal.exception.BizException;

/**
 * Author:zhangyu
 * create on 15/9/21.
 */
public interface SocialService {

    /**
     * 关注用户
     *
     * @param userId 用户ID
     * @param followingId 被关注用户ID
     *
     * */
    void followUser(long userId,long followingId)throws BizException;

    /**
     * 取消关注
     *
     * @param userId 用户ID
     * @param followingId 被关注用户ID
     *
     * */
    void cancelFollowUser(long userId,long followingId)throws BizException;

    /**
     * 得到用户的粉丝
     *
     * @param userId 用户ID
     * @param page 页号
     * @param count 每页记录数
     * */
    Pageable<AccountSimpleDTO> listFollower(long userId,int page,int count)throws BizException;

    /**
     * 得到用户的关注的人
     *
     * @param userId 用户ID
     * @param page 页号
     * @param count 每页记录数
     *
     * */
    Pageable<AccountSimpleDTO> listFollowing(long userId,int page,int count)throws BizException;

    /**
     * 判断当前用户是否关注了target用户
     *
     * @param userId 当前用户ID
     * @param targetId 需要判断的目标用户
     *
     * */
    boolean checkIsFollower(long userId,long targetId)throws BizException;
}
