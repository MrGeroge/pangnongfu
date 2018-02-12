package com.pangnongfu.server.bal.service;

import com.pangnongfu.server.bal.api.SocialService;
import com.pangnongfu.server.bal.dto.AccountSimpleDTO;
import com.pangnongfu.server.bal.dto.Pageable;
import com.pangnongfu.server.bal.exception.BizException;
import com.pangnongfu.server.bal.utils.MD5Util;
import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.AccountDetailDao;
import com.pangnongfu.server.dal.api.RelationDao;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.AccountDetail;
import com.pangnongfu.server.dal.po.Relation;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hao on 2015/9/24.
 */
public class SocialServiceMockTest {

    @Test
    public void testFolloweUser() throws BizException{
        AccountDao accountDao = EasyMock.createMock(AccountDao.class);
        RelationDao relationDao = EasyMock.createMock(RelationDao.class);

        Account follower = new Account();
        follower.setId(1);
        //follower.setUsername("fans");
        //follower.setPassword(MD5Util.encoding("888"));

        Account following = new Account();
        following.setId(2);
        //following.setUsername("idol");
        //follower.setPassword(MD5Util.encoding("888"));

        Relation relation = new Relation();
        relation.setFollower(follower);
        relation.setFollowing(following);

        Relation relation1 = new Relation();
        relation1.setId(1);
        relation1.setFollower(follower);
        relation1.setFollowing(following);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(follower);
        //EasyMock.expect(accountDao.findOne(2L)).andReturn(following);
        EasyMock.expect(relationDao.save(relation)).andReturn(relation1);

        EasyMock.replay(accountDao);
        EasyMock.replay(relationDao);

        SocialService socialService = new SocialServiceImpl().
                setAccountDao(accountDao).
                setRelationDao(relationDao);
        socialService.followUser(1, 2);

        Assert.assertEquals(1,relation1.getId());

    }

    @Test
    public void testCancelFollowUser()throws BizException{
        IMocksControl control = EasyMock.createControl();

        AccountDao accountDao = control.createMock(AccountDao.class);
        RelationDao relationDao = control.createMock(RelationDao.class);

        Account follower = new Account();
        follower.setId(1);
        //follower.setUsername("admin");
        //follower.setPassword(MD5Util.encoding("888"));

        Account following = new Account();
        following.setId(2);

        Relation relation = new Relation();
        relation.setId(1);
        relation.setFollower(follower);
        relation.setFollowing(following);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(follower);
        //EasyMock.expect(accountDao.findOne(2L)).andReturn(following);
        EasyMock.expect(relationDao.findByFollowerAndFollowing(follower, following)).andReturn(relation);
        relationDao.delete(relation);
        EasyMock.expectLastCall();

        control.replay();
        EasyMock.verify();

        SocialService socialService = new SocialServiceImpl().
                setAccountDao(accountDao).
                setRelationDao(relationDao);

        socialService.cancelFollowUser(1, 2);
        Assert.assertEquals(1, relation.getId());
    }

    @Test
    public void testListFollower()throws BizException{
        AccountDao accountDao = EasyMock.createMock(AccountDao.class);
        RelationDao relationDao = EasyMock.createMock(RelationDao.class);
        AccountDetailDao accountDetailDao = EasyMock.createMock(AccountDetailDao.class);

        Account following = new Account();
        following.setId(1);
        //following.setUsername("admin");
        //following.setPassword(MD5Util.encoding("888"));

        List<Relation> content = new ArrayList<>();
        Relation relation1 = new Relation();
        Relation relation2 = new Relation();
        Account follower1 = new Account();
        Account follower2 = new Account();
        AccountDetail detail1 = new AccountDetail();
        AccountDetail detail2 = new AccountDetail();

        follower1.setId(2);
        follower2.setId(3);
        follower1.setUsername("user2");
        follower2.setUsername("user3");
        follower1.setPassword(MD5Util.encoding("777"));
        follower2.setPassword(MD5Util.encoding("777"));
        detail1.setId(2);
        detail2.setId(3);
        detail1.setNickname("username2");
        detail2.setNickname("username3");
        detail1.setAvatar_url("userAva2");
        detail2.setAvatar_url("userAva3");
        detail1.setAccount(follower1);
        detail2.setAccount(follower2);

        relation1.setId(1);
        relation2.setId(2);
        relation1.setFollowing(following);
        relation2.setFollowing(following);
        relation1.setFollower(follower1);
        relation2.setFollower(follower2);

        content.add(relation1);
        //content.add(relation2);

        Page<Relation> page = new PageImpl<>(content,new PageRequest(0,1), 2);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(following);
        EasyMock.expect(relationDao.findByFollowing(following, new PageRequest(0, 1))).andReturn(page);
        EasyMock.expect(accountDetailDao.findByAccount(relation1.getFollower())).andReturn(detail1);
        EasyMock.expect(accountDetailDao.findByAccount(relation2.getFollower())).andReturn(detail2);

        EasyMock.replay(accountDao);
        EasyMock.replay(accountDetailDao);
        EasyMock.replay(relationDao);

        SocialService socialService = new SocialServiceImpl().
                setAccountDao(accountDao).
                setAccountDetailDao(accountDetailDao).
                setRelationDao(relationDao);
        Pageable<AccountSimpleDTO> accountSimpleDTOPageable = socialService.listFollower(1,1,1);

        Assert.assertEquals("username2", accountSimpleDTOPageable.getContent().get(0).getNickname());
    }

    @Test
    public void testListFollowing()throws BizException{
        AccountDao accountDao = EasyMock.createMock(AccountDao.class);
        RelationDao relationDao = EasyMock.createMock(RelationDao.class);
        AccountDetailDao accountDetailDao = EasyMock.createMock(AccountDetailDao.class);

        Account follower = new Account();
        follower.setId(1);
        //follower.setUsername("admin");
        //follower.setPassword(MD5Util.encoding("888"));

        List<Relation> content = new ArrayList<>();
        Relation relation1 = new Relation();
        Relation relation2 = new Relation();
        Account following1 = new Account();
        Account following2 = new Account();
        AccountDetail detail1 = new AccountDetail();
        AccountDetail detail2 = new AccountDetail();

        following1.setId(2);
        following2.setId(3);
        following1.setUsername("user2");
        following2.setUsername("user3");
        following1.setPassword(MD5Util.encoding("777"));
        following2.setPassword(MD5Util.encoding("777"));
        detail1.setId(2);
        detail2.setId(3);
        detail1.setNickname("username2");
        detail2.setNickname("username3");
        detail1.setAvatar_url("userAva2");
        detail2.setAvatar_url("userAva3");
        detail1.setAccount(following1);
        detail2.setAccount(following2);

        relation1.setId(1);
        relation2.setId(2);
        relation1.setFollowing(following1);
        relation2.setFollowing(following2);
        relation1.setFollower(follower);
        relation2.setFollower(follower);

        content.add(relation1);
        content.add(relation2);

        Page<Relation> page = new PageImpl<>(content);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(follower);
        EasyMock.expect(relationDao.findByFollower(follower, new PageRequest(0, 2))).andReturn(page);
        EasyMock.expect(accountDetailDao.findByAccount(relation1.getFollowing())).andReturn(detail1);
        EasyMock.expect(accountDetailDao.findByAccount(relation2.getFollowing())).andReturn(detail2);

        EasyMock.replay(accountDao);
        EasyMock.replay(accountDetailDao);
        EasyMock.replay(relationDao);

        SocialService socialService = new SocialServiceImpl().
                setAccountDao(accountDao).
                setAccountDetailDao(accountDetailDao).
                setRelationDao(relationDao);
        Pageable<AccountSimpleDTO> accountSimpleDTOPageable = socialService.listFollowing(1, 1, 2);

        Assert.assertEquals("username2", accountSimpleDTOPageable.getContent().get(0).getNickname());
    }

    @Test
    public void testCheckIsFollower()throws BizException{
        IMocksControl control = EasyMock.createControl();

        AccountDao accountDao = control.createMock(AccountDao.class);
        RelationDao relationDao = control.createMock(RelationDao.class);

        Account follower = new Account();
        Account following = new Account();
        Relation relation = null;

        follower.setId(1);
        following.setId(2);
        //follower.setUsername("user1");
        //following.setUsername("user2");
        //follower.setPassword(MD5Util.encoding("888"));
        //following.setPassword(MD5Util.encoding("888"));

        relation = new Relation();
        relation.setId(1);
        relation.setFollower(follower);
        relation.setFollowing(following);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(follower);
        //EasyMock.expect(accountDao.findOne(2L)).andReturn(following);
        EasyMock.expect(relationDao.findByFollowerAndFollowing(follower, following)).andReturn(relation);

        control.replay();
        EasyMock.verify();

        SocialService socialService = new SocialServiceImpl().
                setAccountDao(accountDao).
                setRelationDao(relationDao);
        boolean flag = socialService.checkIsFollower(1,2);

        Assert.assertEquals(true, flag);
    }
}
