package com.pangnongfu.server.bal.api;

import com.pangnongfu.server.bal.dto.AccountDetailDTO;
import com.pangnongfu.server.bal.exception.BizException;

/**
 * Author:zhangyu
 * create on 15/9/20.
 */
public interface AccountService {

    /**
     * 用户账户验证
     *
     * @param username 用户名
     * @param password 密码
     *
     * @return 验证用户成功后，账户基本信息
     *
     * @exception BizException errorCode包括ERROR_CODE_USER_NOT_FOUND 、ERROR_CODE_PASSWORD_ERROR
     * */
    AccountDetailDTO validate(String username,String password) throws BizException;


    /**
     * @deprecated
     *
     * 重置密码
     *
     * @param userId 用户ID
     * @param newPassword 新密码
     *
     * @exception BizException errorCode包括ERROR_CODE_USER_NOT_FOUND
     *
     * */
    @Deprecated
    void updatePassword(long userId,String newPassword) throws BizException;

    /**
     * 重置密码
     *
     * @param username 账户名
     * @param newPassword 新密码
     *
     * @exception BizException errorCode包括ERROR_CODE_USER_NOT_FOUND
     *
     * */
    void updatePassword(String username,String newPassword) throws BizException;

    /**
     * 添加新用户
     *
     * @param username 用户名
     * @param password 密码
     *
     * @exception BizException errorCode包括 ERROR_CODE_USERNAME_EXISTED
     * */
    long addAccount(String username,String password) throws BizException;


    /**
     * 更新用户
     *
     * @param info 用户新资料
     *
     * @exception BizException errorCode包括 ERROR_CODE_USER_NOT_FOUND
     * */
    void updateAccountInfo(AccountDetailDTO info) throws BizException;
}
