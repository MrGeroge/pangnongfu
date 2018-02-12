package com.pangnongfu.server.bal.service;

import com.pangnongfu.server.bal.api.SocialService;
import com.pangnongfu.server.bal.dto.AccountSimpleDTO;
import com.pangnongfu.server.bal.dto.Pageable;
import com.pangnongfu.server.bal.exception.BizException;
import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.AccountDetailDao;
import com.pangnongfu.server.dal.api.RelationDao;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.AccountDetail;
import com.pangnongfu.server.dal.po.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hao on 2015/9/23.
 */
public class SocialServiceImpl implements SocialService {

    private Logger logger= LoggerFactory.getLogger(SocialServiceImpl.class);

    RelationDao relationDao;
    AccountDao accountDao;
    AccountDetailDao accountDetailDao;

    public SocialServiceImpl setRelationDao(RelationDao relationDao) {
        this.relationDao = relationDao;
        return this;
    }

    public SocialServiceImpl setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
        return this;
    }

    public SocialServiceImpl setAccountDetailDao(AccountDetailDao accountDetailDao){
        this.accountDetailDao = accountDetailDao;
        return this;
    }

    @Override
    public void followUser(long userId, long followingId) throws BizException{
        //用户ID小于0时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format is incorrect!");
        }

        //被关注者ID小于1时抛出异常
        if(followingId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "followingId format is incorrect!");
        }

        //用户ID和关注者ID相同时抛出异常
        if(userId == followingId){
            throw new BizException(BizException.ERROR_CODE_FOLLOW_SELF, "userId equals followingId!");
        }

        Account follower = new Account();
        follower.setId(userId);

        Account following = new Account();
        following.setId(followingId);

        Relation relation;

        if(follower != null && following != null){

            //建立关注关系并持久到数据库
            relation = new Relation();
            relation.setFollower(follower);
            relation.setFollowing(following);
            relationDao.save(relation);

            logger.info("follow user success followingId:" + String.valueOf(userId));
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "user or following instance not found!");
        }
    }

    @Override
    public void cancelFollowUser(long userId, long followingId) throws BizException{

        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //被关注者ID小于1时抛出异常
        if(followingId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "followingId format incorrect!");
        }

        Relation relation = null;
        Account account = new Account();
        account.setId(userId);

        Account following = new Account();
        following.setId(followingId);

        //根据关注者和被关注者查找relation对象
        relation = relationDao.findByFollowerAndFollowing(account, following);

        if(relation != null){

            //删除两者之间的关系
            relationDao.delete(relation);

            logger.info("cancel fellow user success followingId:" + String.valueOf(followingId));
        }else{
            throw new BizException(BizException.ERROR_CODE_INSTANCE_NOT_FOUND, "relation instance not found!");
        }
    }

    @Override
    public Pageable<AccountSimpleDTO> listFollower(long userId, int page, int count) throws BizException{

        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //分页参数不正确时抛出异常
        if(page < 1 || count < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "paging parameter incorrect!");
        }

        Account following = new Account();
        following.setId(userId);

        //根据分页条件查找relation分页集合
        Page<Relation> page1 = relationDao.findByFollowing(following, new PageRequest(page-1, count));

        //新建Pageable对象用于返回
        Pageable<AccountSimpleDTO> accountSimpleDTOPage = new Pageable<>();

        List<AccountSimpleDTO> content = new LinkedList<>();
        AccountSimpleDTO accountSimpleDTO;

        AccountDetail detail = null;

        if(page1.getContent().size() != 0) {

            //若得到分页集合不为空，整合结果到Pageable对象中
            for (Relation relation : page1.getContent()) {
                detail = accountDetailDao.findByAccount(relation.getFollower());
                accountSimpleDTO = new AccountSimpleDTO();
                accountSimpleDTO.setUserId(detail.getId());
                accountSimpleDTO.setNickname(detail.getNickname());
                accountSimpleDTO.setAvatarUrl(detail.getAvatar_url());
                content.add(accountSimpleDTO);
            }
            accountSimpleDTOPage.setAllCount((int) page1.getTotalElements());
            accountSimpleDTOPage.setContent(content);
            accountSimpleDTOPage.setCurrentPage(page);
            accountSimpleDTOPage.setMaxPage(page1.getTotalPages());
        }else{

            //若分页结果为空，单独设置Pageable对象属性
            accountSimpleDTOPage.setAllCount(0);
            accountSimpleDTOPage.setCurrentPage(page);
            accountSimpleDTOPage.setMaxPage(0);
            accountSimpleDTOPage.setContent(content);
        }

        logger.info("list followers of user:" + String.valueOf(userId));
        for(AccountSimpleDTO accountSimple : accountSimpleDTOPage.getContent()){
            logger.info("follower:" + String.valueOf(accountSimple.getUserId()));
        }
        return accountSimpleDTOPage;
    }

    @Override
    public Pageable<AccountSimpleDTO> listFollowing(long userId, int page, int count) throws BizException{
        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //分页参数不正确时抛出异常
        if(page < 1 || count < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "paging parameter incorrect!");
        }

        Account follower = new Account();
        follower.setId(userId);

        //根据分页条件查找关注者的Relation集合
        Page<Relation> page1 = relationDao.findByFollower(follower, new PageRequest(page-1, count));

        //新建Pageable对象用于返回
        Pageable<AccountSimpleDTO> accountSimpleDTOPage = new Pageable<>();
        List<AccountSimpleDTO> content = new ArrayList<>();
        AccountSimpleDTO accountSimpleDTO;

        AccountDetail detail = null;

        if(page1.getContent().size() != 0) {

            //若得到分页集合不为空，整合结果到Pageable对象中
            for (Relation relation : page1.getContent()) {
                detail = accountDetailDao.findByAccount(relation.getFollowing());
                accountSimpleDTO = new AccountSimpleDTO();
                accountSimpleDTO.setUserId(detail.getId());
                accountSimpleDTO.setNickname(detail.getNickname());
                accountSimpleDTO.setAvatarUrl(detail.getAvatar_url());
                content.add(accountSimpleDTO);
            }

            accountSimpleDTOPage.setAllCount((int) page1.getTotalElements());
            accountSimpleDTOPage.setContent(content);
            accountSimpleDTOPage.setCurrentPage(page);
            accountSimpleDTOPage.setMaxPage(page1.getTotalPages());
        }else{

            //若分页结果为空，单独设置Pageable对象属性
            accountSimpleDTOPage.setAllCount(0);
            accountSimpleDTOPage.setCurrentPage(page);
            accountSimpleDTOPage.setMaxPage(0);
            accountSimpleDTOPage.setContent(null);
        }

        logger.info("list followings of user:" + String.valueOf(userId));
        for(AccountSimpleDTO accountSimple : accountSimpleDTOPage.getContent()){
            logger.info("following:" + String.valueOf(accountSimple.getUserId()));
        }
        return accountSimpleDTOPage;
    }

    @Override
    public boolean checkIsFollower(long userId, long targetId) throws BizException{

        //用户ID小于1时抛出异常
        if(userId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "userId format incorrect!");
        }

        //被关注者ID小于1时抛出异常
        if(targetId < 1){
            throw new BizException(BizException.ERROR_CODE_NULL_POINTER, "targetId format incorrect!");
        }

        boolean flag = false;
        Account follower = new Account();
        follower.setId(userId);

        Account following = new Account();
        following.setId(targetId);

        //根据用户和目标用户查找两者之间是否存在关系
        Relation relation = relationDao.findByFollowerAndFollowing(follower, following);
        if(relation != null){
            flag = true;
        }

        logger.info("check is follower? "+ String.valueOf(flag));
        return flag;
    }
}
