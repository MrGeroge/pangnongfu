package com.pangnongfu.server.bal.service;

import com.pangnongfu.server.bal.api.AccountService;
import com.pangnongfu.server.bal.dto.AccountDetailDTO;
import com.pangnongfu.server.bal.exception.BizException;
import com.pangnongfu.server.bal.utils.MD5Util;
import com.pangnongfu.server.dal.api.*;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.AccountDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by hao on 2015/9/22.
 * Modified by zhangyu on 2015/9/23
 *
 */
public class AccountServiceImpl implements AccountService {

    private static Logger logger= LoggerFactory.getLogger(AccountServiceImpl.class);

    private AccountDao accountDao;

    private AccountDetailDao accountDetailDao;

    public AccountServiceImpl setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
        return this;
    }

    public AccountServiceImpl setAccountDetailDao(AccountDetailDao accountDetailDao) {
        this.accountDetailDao = accountDetailDao;
        return this;
    }

    @Override
    public AccountDetailDTO validate(String username, String password) throws BizException {

        Account account = null;

        //用户名为空或者密码为空时抛出异常
        if(username != null && !"".equals(username)) {
            account = accountDao.findByUsername(username);
            if(password == null || "".equals(password)){
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "password is null!");
            }
        }else{
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "username is null!");
        }

        //账户验证
        if(account!=null){
            if(account.getPassword().equals(MD5Util.encoding(password))){
                AccountDetailDTO detailDTO=new AccountDetailDTO();
                detailDTO.setUserId(account.getId());

                //根据账户查找用户详细信息
                AccountDetail detail=accountDetailDao.findByAccount(account);

                //获取用户的详细信息返回
                detailDTO.setNickname(detail.getNickname());
                detailDTO.setAvatarUrl(detail.getAvatar_url());
                detailDTO.setCity(detail.getCity());
                detailDTO.setDistrict(detail.getDistrict());
                detailDTO.setGender(
                        detail.getGender()==AccountDetail.GENDER_MALE?
                                AccountDetailDTO.GENDER_MALE:AccountDetailDTO.GENDER_FEMALE);
                detailDTO.setAbout(detail.getAbout());

                logger.info("account validate success user:" + detailDTO.getUserId());
                return detailDTO;
            }else {
                throw new BizException(BizException.ERROR_CODE_PASSWORD_ERROR,"password wrong!");
            }
        }else{
            throw new BizException(BizException.ERROR_CODE_USER_NOT_FOUND,"user not exist!");
        }
    }

    @Override
    public void updatePassword(long userId, String newPassword) throws BizException {
        Account account = null;

        //用户ID或新密码为空时抛出异常
        if(userId > 0) {
            account = accountDao.findOne(userId);
            if(newPassword == null || "".equals(newPassword)){
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "new password is null!");
            }
        }else{
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format is incorrect!");
        }

        if(account != null){

            //设置账户加密后密码并保存
            account.setPassword(MD5Util.encoding(newPassword));
            accountDao.save(account);
            logger.info("update password success "+ String.valueOf(userId));
        }else{
            throw new BizException(BizException.ERROR_CODE_USER_NOT_FOUND,"user not exist!");
        }
    }

    @Override
    public void updatePassword(String username, String newPassword) throws BizException {
        Account account = null;

        //用户名或新密码为空时抛出异常
        if(username != null && !"".equals(username)) {
            account = accountDao.findByUsername(username);
            if(newPassword == null || "".equals(newPassword)){
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "new password is null!");
            }
        }else{
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "username is null!");
        }

        if(account != null){

            //设置账户加密后密码并保存
            account.setPassword(MD5Util.encoding(newPassword));
            accountDao.save(account);
            logger.info("update password success " + String.valueOf(username));
        }else{
            throw new BizException(BizException.ERROR_CODE_USER_NOT_FOUND,"user not exist!");
        }
    }

    @Override
    public long addAccount(String username, String password) throws BizException {
        Account account = null;

        //用户名或密码为空时抛出异常
        if(username != null && !"".equals(username)) {
            account = accountDao.findByUsername(username);
            if(password == null || "".equals(password)){
                throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "password is null!");
            }
        }else{
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "username is null!");
        }

        if(account == null){
            //保存基本账号信息
            account = new Account();
            account.setUsername(username);
            account.setPassword(MD5Util.encoding(password));
            account = accountDao.save(account);

            //创建详细资料
            AccountDetail accountDetail=new AccountDetail();

            //生成随机的用户名
            accountDetail.setNickname("pnf_"+String.valueOf(new Random().nextInt(100)));
            accountDetail.setPhone(username);
            accountDetail.setAccount(account);
            accountDetailDao.save(accountDetail);

            logger.info("add account success " + String.valueOf(username));
            return account.getId();
        }else{
            throw new BizException(BizException.ERROR_CODE_USERNAME_EXISTED,"user already exists!");
        }
    }

    @Override
    public void updateAccountInfo(AccountDetailDTO info) throws BizException {
        //详细信息账户ID为空时抛出异常
        if(info.getUserId() < 0){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "account info id is null!");
        }

        Account account = new Account();
        account.setId(info.getUserId());
        AccountDetail detail = accountDetailDao.findByAccount(account);

        if(detail != null) {

            //更新用户详细信息
            detail.setNickname(info.getNickname());
            detail.setGender(info.getGender().equals(AccountDetailDTO.GENDER_FEMALE) ? AccountDetail.GENDER_FEMALE : AccountDetail.GENDER_MALE);
            detail.setAvatar_url(info.getAvatarUrl());
            detail.setCity(info.getCity());
            detail.setDistrict(info.getDistrict());
            detail.setAbout(info.getAbout());

            //保存用户信息
            accountDetailDao.save(detail);

            logger.info("update account info success " + detail.getId());
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "account detail instance not found");
        }
    }

}
