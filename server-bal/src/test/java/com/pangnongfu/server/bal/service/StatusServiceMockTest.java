package com.pangnongfu.server.bal.service;

import com.pangnongfu.server.bal.api.StatusService;
import com.pangnongfu.server.bal.dto.CommentDTO;
import com.pangnongfu.server.bal.dto.Pageable;
import com.pangnongfu.server.bal.dto.StatusCategoryDTO;
import com.pangnongfu.server.bal.dto.StatusDTO;
import com.pangnongfu.server.bal.exception.BizException;
import com.pangnongfu.server.bal.utils.MD5Util;
import com.pangnongfu.server.dal.api.*;
import com.pangnongfu.server.dal.po.*;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

/**
 * Created by hao on 2015/9/26.
 */
public class StatusServiceMockTest {

    @Test
    public void testListPublicStatuses() throws BizException{
        StatusDao statusDao = EasyMock.createMock(StatusDao.class);
        AccountDetailDao accountDetailDao = EasyMock.createMock(AccountDetailDao.class);
        StatusImageDao statusImageDao = EasyMock.createMock(StatusImageDao.class);

        Account publisher1 = new Account();
        Account publisher2 = new Account();
        Account publisher3 = new Account();
        Account publisher4 = new Account();
        publisher1.setId(1);
        publisher2.setId(2);
        publisher3.setId(3);
        publisher4.setId(4);
        publisher1.setUsername("admin1");
        publisher2.setUsername("admin2");
        publisher3.setUsername("admin3");
        publisher4.setUsername("admin4");
        publisher1.setPassword(MD5Util.encoding("888"));
        publisher2.setPassword(MD5Util.encoding("888"));
        publisher3.setPassword(MD5Util.encoding("888"));
        publisher4.setPassword(MD5Util.encoding("888"));

        AccountDetail detail1 = new AccountDetail();
        AccountDetail detail2 = new AccountDetail();
        AccountDetail detail3 = new AccountDetail();
        AccountDetail detail4 = new AccountDetail();
        detail1.setId(1);
        detail2.setId(2);
        detail3.setId(3);
        detail4.setId(4);
        detail1.setAccount(publisher1);
        detail2.setAccount(publisher2);
        detail3.setAccount(publisher3);
        detail4.setAccount(publisher4);

        StatusCategory statusCategory = new StatusCategory();
        statusCategory.setId(1);
        statusCategory.setTitle("农家趣味");
        statusCategory.setImg_url("www.baidu.com");

        Status status1 = new Status();
        Status status2 = new Status();
        Status status3 = new Status();
        Status status4 = new Status();
        status1.setId(1L);
        status2.setId(2L);
        status3.setId(3L);
        status4.setId(4L);
        status1.setStatusPublisher(publisher1);
        status2.setStatusPublisher(publisher2);
        status3.setStatusPublisher(publisher3);
        status4.setStatusPublisher(publisher4);
        status1.setStatusCategory(statusCategory);
        status2.setStatusCategory(statusCategory);
        status3.setStatusCategory(statusCategory);
        status4.setStatusCategory(statusCategory);

        StatusImage statusImage1 = new StatusImage();
        StatusImage statusImage2 = new StatusImage();
        StatusImage statusImage3 = new StatusImage();
        StatusImage statusImage4 = new StatusImage();
        statusImage1.setId(1);
        statusImage2.setId(2);
        statusImage3.setId(3);
        statusImage4.setId(4);
        statusImage1.setStatus(status1);
        statusImage2.setStatus(status1);
        statusImage3.setStatus(status1);
        statusImage4.setStatus(status1);
        statusImage1.setQuality(1);
        statusImage2.setQuality(1);
        statusImage3.setQuality(1);
        statusImage4.setQuality(1);
        statusImage1.setUrl("pangnongfu");
        statusImage2.setUrl("pangnongfu");
        statusImage3.setUrl("pangnongfu");
        statusImage4.setUrl("pangnongfu");
        List<StatusImage> list = new LinkedList<>();
        list.add(statusImage1);
        list.add(statusImage2);
        list.add(statusImage3);
        list.add(statusImage4);

        List<Status> content = new LinkedList<>();
        content.add(status1);
        content.add(status2);
        content.add(status3);
        //content.add(status4);

        Page<Status> page = new PageImpl<Status>(content,new PageRequest(0, 3,new Sort(Sort.Direction.DESC,"id")),4);

        EasyMock.expect(statusDao.findAll(new PageRequest(0, 3, new Sort(Sort.Direction.DESC, "id")))).andReturn(page);
        EasyMock.expect(accountDetailDao.findByAccount(publisher1)).andReturn(detail1);
        EasyMock.expect(accountDetailDao.findByAccount(publisher2)).andReturn(detail2);
        EasyMock.expect(accountDetailDao.findByAccount(publisher3)).andReturn(detail3);
        EasyMock.expect(accountDetailDao.findByAccount(publisher4)).andReturn(detail4);
        EasyMock.expect(statusImageDao.findByStatus(status1)).andReturn(list);
        EasyMock.expect(statusImageDao.findByStatus(status2)).andReturn(null);
        EasyMock.expect(statusImageDao.findByStatus(status3)).andReturn(null);
        EasyMock.expect(statusImageDao.findByStatus(status4)).andReturn(null);
        EasyMock.replay(statusDao);
        EasyMock.replay(accountDetailDao);
        EasyMock.replay(statusImageDao);

        StatusService statusService = new StatusServiceImpl().
                setStatusDao(statusDao).
                setAccountDetailDao(accountDetailDao).
                setStatusImageDao(statusImageDao);

        Pageable<StatusDTO> pageable = statusService.listPublicStatuses(1,3);

        Assert.assertEquals("pangnongfu", pageable.getContent().get(0).getImages().get(0).getUrl());

    }

    @Test
    public void testListStatusesByUser() throws BizException{
        StatusDao statusDao = EasyMock.createMock(StatusDao.class);
        AccountDao accountDao = EasyMock.createMock(AccountDao.class);
        StatusImageDao statusImageDao = EasyMock.createMock(StatusImageDao.class);
        AccountDetailDao accountDetailDao = EasyMock.createMock(AccountDetailDao.class);

        Account publisher1 = new Account();
        publisher1.setId(1);
        //publisher1.setUsername("admin1");
        //publisher1.setPassword(MD5Util.encoding("888"));

        AccountDetail detail1 = new AccountDetail();
        detail1.setId(1);
        detail1.setAccount(publisher1);

        StatusCategory statusCategory = new StatusCategory();
        statusCategory.setId(1);
        statusCategory.setTitle("农家趣味");
        statusCategory.setImg_url("www.baidu.com");

        Status status1 = new Status();
        Status status2 = new Status();
        Status status3 = new Status();
        Status status4 = new Status();
        status1.setId(1L);
        status2.setId(2L);
        status3.setId(3L);
        status4.setId(4L);
        status1.setStatusPublisher(publisher1);
        status2.setStatusPublisher(publisher1);
        status3.setStatusPublisher(publisher1);
        status4.setStatusPublisher(publisher1);
        status1.setStatusCategory(statusCategory);
        status2.setStatusCategory(statusCategory);
        status3.setStatusCategory(statusCategory);
        status4.setStatusCategory(statusCategory);

        StatusImage statusImage1 = new StatusImage();
        StatusImage statusImage2 = new StatusImage();
        StatusImage statusImage3 = new StatusImage();
        StatusImage statusImage4 = new StatusImage();
        statusImage1.setId(1);
        statusImage2.setId(2);
        statusImage3.setId(3);
        statusImage4.setId(4);
        statusImage1.setStatus(status1);
        statusImage2.setStatus(status1);
        statusImage3.setStatus(status1);
        statusImage4.setStatus(status1);
        statusImage1.setQuality(1);
        statusImage2.setQuality(1);
        statusImage3.setQuality(1);
        statusImage4.setQuality(1);
        statusImage1.setUrl("pangnongfu");
        statusImage2.setUrl("pangnongfu");
        statusImage3.setUrl("pangnongfu");
        statusImage4.setUrl("pangnongfu");
        List<StatusImage> list = new LinkedList<>();
        list.add(statusImage1);
        list.add(statusImage2);
        list.add(statusImage3);
        list.add(statusImage4);

        List<Status> content = new LinkedList<>();
        content.add(status1);
        content.add(status2);
        content.add(status3);
        //content.add(status4);

        Page<Status> page = new PageImpl<Status>(content,new PageRequest(0, 3,new Sort(Sort.Direction.DESC,"id")),4);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(publisher1);
        EasyMock.expect(statusDao.findByStatusPublisher(publisher1, new PageRequest(0, 3, new Sort(Sort.Direction.DESC, "id")))).andReturn(page);
        EasyMock.expect(accountDetailDao.findByAccount(publisher1)).andReturn(detail1);
        EasyMock.expect(statusImageDao.findByStatus(status1)).andReturn(list);
        EasyMock.expect(accountDetailDao.findByAccount(publisher1)).andReturn(detail1);
        EasyMock.expect(statusImageDao.findByStatus(status2)).andReturn(null);
        EasyMock.expect(accountDetailDao.findByAccount(publisher1)).andReturn(detail1);
        EasyMock.expect(statusImageDao.findByStatus(status3)).andReturn(null);
        //EasyMock.expect(accountDetailDao.findByAccount(publisher4)).andReturn(detail4);
        EasyMock.replay(statusDao);
        EasyMock.replay(accountDao);
        EasyMock.replay(statusImageDao);
        EasyMock.replay(accountDetailDao);
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setStatusDao(statusDao).
                setAccountDao(accountDao).
                setAccountDetailDao(accountDetailDao).
                setStatusImageDao(statusImageDao);

        Pageable<StatusDTO> pageable = statusService.listStatusesByUser(1,1,3);

        Assert.assertEquals(1, pageable.getContent().get(0).getPublisherId().getUserId());
    }

    @Test
    public void testListStatusesByCategory() throws BizException{
        StatusDao statusDao = EasyMock.createMock(StatusDao.class);
        StatusCategoryDao statusCategoryDao = EasyMock.createMock(StatusCategoryDao.class);
        AccountDetailDao accountDetailDao = EasyMock.createMock(AccountDetailDao.class);
        StatusImageDao statusImageDao = EasyMock.createMock(StatusImageDao.class);

        Account publisher1 = new Account();
        Account publisher2 = new Account();
        Account publisher3 = new Account();
        Account publisher4 = new Account();
        publisher1.setId(1);
        publisher2.setId(2);
        publisher3.setId(3);
        publisher4.setId(4);
        publisher1.setUsername("admin1");
        publisher2.setUsername("admin2");
        publisher3.setUsername("admin3");
        publisher4.setUsername("admin4");
        publisher1.setPassword(MD5Util.encoding("888"));
        publisher2.setPassword(MD5Util.encoding("888"));
        publisher3.setPassword(MD5Util.encoding("888"));
        publisher4.setPassword(MD5Util.encoding("888"));

        AccountDetail detail1 = new AccountDetail();
        AccountDetail detail2 = new AccountDetail();
        AccountDetail detail3 = new AccountDetail();
        AccountDetail detail4 = new AccountDetail();
        detail1.setId(1);
        detail2.setId(2);
        detail3.setId(3);
        detail4.setId(4);
        detail1.setAccount(publisher1);
        detail2.setAccount(publisher2);
        detail3.setAccount(publisher3);
        detail4.setAccount(publisher4);

        StatusCategory statusCategory1 = new StatusCategory();
        statusCategory1.setId(1);
        //statusCategory1.setTitle("pangnongfu");

        StatusCategory statusCategory2 = new StatusCategory();
        statusCategory2.setId(1);
        statusCategory2.setImg_url("www.sino.com");
        statusCategory2.setTitle("农家");

        Status status1 = new Status();
        Status status2 = new Status();
        Status status3 = new Status();
        Status status4 = new Status();
        status1.setId(1L);
        status2.setId(2L);
        status3.setId(3L);
        status4.setId(4L);
        status1.setStatusPublisher(publisher1);
        status2.setStatusPublisher(publisher2);
        status3.setStatusPublisher(publisher3);
        status4.setStatusPublisher(publisher4);
        status1.setStatusCategory(statusCategory2);
        status2.setStatusCategory(statusCategory2);
        status3.setStatusCategory(statusCategory2);
        status4.setStatusCategory(statusCategory2);

        StatusImage statusImage1 = new StatusImage();
        StatusImage statusImage2 = new StatusImage();
        StatusImage statusImage3 = new StatusImage();
        StatusImage statusImage4 = new StatusImage();
        statusImage1.setId(1);
        statusImage2.setId(2);
        statusImage3.setId(3);
        statusImage4.setId(4);
        statusImage1.setStatus(status1);
        statusImage2.setStatus(status1);
        statusImage3.setStatus(status1);
        statusImage4.setStatus(status1);
        statusImage1.setQuality(1);
        statusImage2.setQuality(1);
        statusImage3.setQuality(1);
        statusImage4.setQuality(1);
        statusImage1.setUrl("pangnongfu");
        statusImage2.setUrl("pangnongfu");
        statusImage3.setUrl("pangnongfu");
        statusImage4.setUrl("pangnongfu");
        List<StatusImage> list = new LinkedList<>();
        list.add(statusImage1);
        list.add(statusImage2);
        list.add(statusImage3);
        list.add(statusImage4);

        List<Status> content = new LinkedList<>();
        content.add(status1);
        content.add(status2);
        content.add(status3);
        //content.add(status4);


        Page<Status> page = new PageImpl<Status>(content,new PageRequest(0, 3,new Sort(Sort.Direction.DESC,"id")),4);

        //EasyMock.expect(statusCategoryDao.findOne(1L)).andReturn(statusCategory1);
        EasyMock.expect(statusDao.findByStatusCategory(statusCategory1, new PageRequest(0, 3, new Sort(Sort.Direction.DESC, "id")))).andReturn(page);
        EasyMock.expect(accountDetailDao.findByAccount(publisher1)).andReturn(detail1);
        EasyMock.expect(statusImageDao.findByStatus(status1)).andReturn(list);
        EasyMock.expect(accountDetailDao.findByAccount(publisher2)).andReturn(detail2);
        EasyMock.expect(statusImageDao.findByStatus(status2)).andReturn(null);
        EasyMock.expect(accountDetailDao.findByAccount(publisher3)).andReturn(detail3);
        EasyMock.expect(statusImageDao.findByStatus(status3)).andReturn(null);
        EasyMock.expect(accountDetailDao.findByAccount(publisher4)).andReturn(detail4);
        EasyMock.replay(statusDao);
        EasyMock.replay(accountDetailDao);
        EasyMock.replay(statusCategoryDao);
        EasyMock.replay(statusImageDao);

        StatusService statusService = new StatusServiceImpl().
                setStatusDao(statusDao).
                setAccountDetailDao(accountDetailDao).
                setStatusCategoryDao(statusCategoryDao).
                setStatusImageDao(statusImageDao);

        Pageable<StatusDTO> pageable = statusService.listStatusesByCategory(1, 1, 3);

        Assert.assertEquals(3, pageable.getContent().size());
    }

    @Test
    public void testListCollectedStatuses() throws BizException{
        IMocksControl control = EasyMock.createControl();

        AccountDao accountDao = control.createMock(AccountDao.class);
        StatusCollectDao statusCollectDao = control.createMock(StatusCollectDao.class);
        AccountDetailDao accountDetailDao = control.createMock(AccountDetailDao.class);
        StatusImageDao statusImageDao = control.createMock(StatusImageDao.class);

        Account account1 = new Account();
        Account account2 = new Account();
        account1.setId(1);
        account2.setId(2);
        //account1.setUsername("admin1");
        account2.setUsername("admin2");
        //account1.setPassword(MD5Util.encoding("888"));
        account2.setPassword(MD5Util.encoding("888"));

        AccountDetail accountDetail1 = new AccountDetail();
        AccountDetail accountDetail2 = new AccountDetail();
        accountDetail1.setId(1);
        accountDetail2.setId(2);
        accountDetail1.setAccount(account1);
        accountDetail2.setAccount(account2);

        StatusCategory statusCategory2 = new StatusCategory();
        statusCategory2.setId(1);
        statusCategory2.setImg_url("www.sino.com");
        statusCategory2.setTitle("农家");

        /*StatusImage statusImage1 = new StatusImage();
        StatusImage statusImage2 = new StatusImage();
        statusImage1.setId(1);
        statusImage2.setId(2);
        Set<StatusImage> set = new HashSet<StatusImage>();
        set.add(statusImage1);
        set.add(statusImage2);*/

        Status status1 = new Status();
        Status status2 = new Status();
        Status status3 = new Status();
        Status status4 = new Status();
        status1.setId(1);
        status2.setId(2);
        status3.setId(3);
        status4.setId(4);
        status1.setStatusPublisher(account2);
        status2.setStatusPublisher(account2);
        status3.setStatusPublisher(account2);
        status4.setStatusPublisher(account2);
        //status1.setListStatusImage(set);
        //status2.setListStatusImage(set);
        //status3.setListStatusImage(set);
        //status4.setListStatusImage(set);
        status1.setStatusCategory(statusCategory2);
        status2.setStatusCategory(statusCategory2);
        status3.setStatusCategory(statusCategory2);
        status4.setStatusCategory(statusCategory2);

        StatusImage statusImage1 = new StatusImage();
        StatusImage statusImage2 = new StatusImage();
        statusImage1.setId(1);
        statusImage2.setId(2);
        statusImage1.setStatus(status1);
        statusImage2.setStatus(status1);
        List<StatusImage> l = new LinkedList<>();
        l.add(statusImage1);
        l.add(statusImage2);

        StatusCollect statusCollect1 = new StatusCollect();
        StatusCollect statusCollect2 = new StatusCollect();
        StatusCollect statusCollect3 = new StatusCollect();
        StatusCollect statusCollect4 = new StatusCollect();
        statusCollect1.setId(1);
        statusCollect2.setId(2);
        statusCollect3.setId(3);
        statusCollect4.setId(4);
        statusCollect1.setAccountCollect(account1);
        statusCollect2.setAccountCollect(account1);
        statusCollect3.setAccountCollect(account1);
        statusCollect4.setAccountCollect(account1);
        statusCollect1.setStatus(status1);
        statusCollect2.setStatus(status2);
        statusCollect3.setStatus(status3);
        statusCollect4.setStatus(status4);
        List<StatusCollect> list = new LinkedList<>();
        list.add(statusCollect1);
        list.add(statusCollect2);
        list.add(statusCollect3);
        //list.add(statusCollect4);

        Page<StatusCollect> page = new PageImpl<StatusCollect>(list , new PageRequest(0,3,new Sort(Sort.Direction.DESC,"id")),4);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(account1);
        EasyMock.expect(statusCollectDao.findByAccountCollect(account1, new PageRequest(0, 3, new Sort(Sort.Direction.DESC, "id")))).andReturn(page);
        EasyMock.expect(accountDetailDao.findByAccount(account2)).andReturn(accountDetail2);
        EasyMock.expect(statusImageDao.findByStatus(status1)).andReturn(l);
        EasyMock.expect(accountDetailDao.findByAccount(account2)).andReturn(accountDetail2);
        EasyMock.expect(statusImageDao.findByStatus(status2)).andReturn(null);
        EasyMock.expect(accountDetailDao.findByAccount(account2)).andReturn(accountDetail2);
        EasyMock.expect(statusImageDao.findByStatus(status3)).andReturn(null);

        control.replay();
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setAccountDao(accountDao).
                setAccountDetailDao(accountDetailDao).
                setStatusCollectDao(statusCollectDao).
                setStatusImageDao(statusImageDao);

        Pageable<StatusDTO> pageable = statusService.listCollectedStatuses(1, 1, 3);

        Assert.assertEquals(3, pageable.getContent().size());
    }

    @Test
    public void testAddStatus()throws BizException{

    }

    @Test
    public void testDeleteStatus()throws BizException{
        StatusDao statusDao = EasyMock.createMock(StatusDao.class);
        StatusCategoryDao statusCategoryDao = EasyMock.createMock(StatusCategoryDao.class);

        Account account = new Account();
        account.setId(1);
        account.setUsername("admin");
        account.setPassword(MD5Util.encoding("888"));

        StatusCategory statusCategory = new StatusCategory();
        statusCategory.setId(1);
        statusCategory.setTitle("pangnongfu");
        statusCategory.setImg_url("abc");
        statusCategory.setStatus_num(4);

        StatusCategory statusCategory1 = new StatusCategory();
        statusCategory1.setId(1);
        statusCategory1.setTitle("pangnongfu");
        statusCategory1.setImg_url("abc");
        statusCategory1.setStatus_num(3);

        Status status = new Status();
        status.setId(1);
        status.setStatusPublisher(account);
        status.setStatusCategory(statusCategory);

        EasyMock.expect(statusDao.findOne(1L)).andReturn(status);
        statusDao.delete(1L);
        EasyMock.expectLastCall();
        EasyMock.expect(statusCategoryDao.save(statusCategory1)).andReturn(statusCategory1);

        EasyMock.replay(statusDao);
        EasyMock.replay(statusCategoryDao);
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setStatusDao(statusDao).
                setStatusCategoryDao(statusCategoryDao);
        statusService.deleteStatus(1);

        Assert.assertEquals(3, status.getStatusCategory().getStatus_num());
    }

    @Test
    public void testAddComment()throws BizException{

        /**单元测试不成功，因为Comment中有时间参数，不能保持一致*/

        IMocksControl control = EasyMock.createControl();

        AccountDao accountDao = control.createMock(AccountDao.class);
        StatusDao statusDao = control.createMock(StatusDao.class);
        StatusCommentDao statusCommentDao = control.createMock(StatusCommentDao.class);

        Account account = new Account();
        account.setId(1);
        //account.setUsername("user");
        //account.setPassword(MD5Util.encoding("888"));

        StatusCategory statusCategory = new StatusCategory();
        statusCategory.setId(1);
        statusCategory.setStatus_num(3);
        statusCategory.setTitle("pangnongfu");
        statusCategory.setImg_url("abc");

        Date date = new Date();
        Status status = new Status();
        status.setId(1);
        status.setComment_num(4);
        status.setStatusPublisher(account);
        status.setStatusCategory(statusCategory);
        status.setCreated_at(date);
        status.setText("aaa");
        status.setLongitude(123);
        status.setLatitude(345);
        status.setAddress("address");

        Status status1 = new Status();
        status1.setId(1);
        status1.setComment_num(5);
        status1.setStatusPublisher(account);
        status1.setStatusCategory(statusCategory);
        status1.setCreated_at(date);
        status1.setText("aaa");
        status1.setLongitude(123);
        status1.setLatitude(344);
        status1.setAddress("address");


        StatusComment statusComment1 = new StatusComment();
        StatusComment statusComment2 = new StatusComment();
        //statusComment.setId(1);
        statusComment1.setStatus(status);
        statusComment1.setContent("ddd");
        statusComment1.setCreate_at(new Date());
        statusComment1.setCommentPublisher(account);
        statusComment2.setId(1);
        statusComment2.setStatus(status);
        statusComment2.setContent("ddd");
        statusComment2.setCreate_at(statusComment1.getCreate_at());
        statusComment2.setCommentPublisher(account);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(account);
        EasyMock.expect(statusDao.findOne(1L)).andReturn(status);
        EasyMock.expect(statusCommentDao.save(statusComment1)).andReturn(statusComment2);
        EasyMock.expect(statusDao.save(status1)).andReturn(status1);

        control.replay();
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setAccountDao(accountDao).
                setStatusDao(statusDao).
                setStatusCommentDao(statusCommentDao);
        statusService.addComment(1,1,"ddd");

        Assert.assertEquals(1 ,statusComment2.getId());
    }

    @Test
    public void testLikeStatus()throws BizException{
        IMocksControl control = EasyMock.createControl();
        AccountDao accountDao = control.createMock(AccountDao.class);
        StatusDao statusDao = control.createMock(StatusDao.class);
        StatusLoveDao statusLoveDao = control.createMock(StatusLoveDao.class);

        Account account = new Account();
        account.setId(1);
        //account.setUsername("user");
        //account.setPassword(MD5Util.encoding("888"));

        StatusCategory statusCategory = new StatusCategory();
        statusCategory.setId(1);
        statusCategory.setStatus_num(3);
        statusCategory.setTitle("pangnongfu");
        statusCategory.setImg_url("abc");

        Date date = new Date();
        Status status = new Status();
        status.setId(1);
        status.setLove_num(4);
        status.setStatusPublisher(account);
        status.setStatusCategory(statusCategory);
        status.setCreated_at(date);
        status.setText("aaa");
        status.setLongitude(123);
        status.setLatitude(45);
        status.setAddress("address");

        Status status1 = new Status();
        status1.setId(1);
        status1.setLove_num(5);
        status1.setStatusPublisher(account);
        status1.setStatusCategory(statusCategory);
        status1.setCreated_at(date);
        status1.setText("aaa");
        status1.setLongitude(123);
        status1.setLatitude(45);
        status1.setAddress("address");

        StatusLove statusLove1 = new StatusLove();
        StatusLove statusLove2 = new StatusLove();
        //statusLove1.setId(1);
        statusLove2.setId(2);
        statusLove1.setAccountLove(account);
        statusLove2.setAccountLove(account);
        statusLove1.setStatus(status);
        statusLove2.setStatus(status);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(account);
        EasyMock.expect(statusDao.findOne(1L)).andReturn(status);
        EasyMock.expect(statusLoveDao.save(statusLove1)).andReturn(statusLove2);
        EasyMock.expect(statusDao.save(status1)).andReturn(status1);

        control.replay();
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setAccountDao(accountDao).
                setStatusDao(statusDao).
                setStatusLoveDao(statusLoveDao);

        statusService.likeStatus(1, 1);
        Assert.assertEquals(2, statusLove2.getId());
    }

    @Test
    public void testUnlikeStatus()throws BizException{
        IMocksControl control = EasyMock.createControl();
        AccountDao accountDao = control.createMock(AccountDao.class);
        StatusDao statusDao = control.createMock(StatusDao.class);
        StatusLoveDao statusLoveDao = control.createMock(StatusLoveDao.class);

        Account account = new Account();
        account.setId(1);
        //account.setUsername("user");
        //account.setPassword(MD5Util.encoding("888"));

        StatusCategory statusCategory = new StatusCategory();
        statusCategory.setId(1);
        statusCategory.setStatus_num(3);
        statusCategory.setTitle("pangnongfu");
        statusCategory.setImg_url("abc");

        Date date = new Date();
        Status status = new Status();
        status.setId(1);
        status.setLove_num(4);
        status.setStatusPublisher(account);
        status.setStatusCategory(statusCategory);
        status.setCreated_at(date);
        status.setText("aaa");
        status.setLongitude(123);
        status.setLatitude(34);
        status.setAddress("address");

        Status status1 = new Status();
        status1.setId(1);
        status1.setLove_num(3);
        status1.setStatusPublisher(account);
        status1.setStatusCategory(statusCategory);
        status1.setCreated_at(date);
        status1.setText("aaa");
        status1.setLongitude(123);
        status1.setLatitude(34);
        status1.setAddress("address");

        StatusLove statusLove1 = new StatusLove();
        statusLove1.setId(1);
        statusLove1.setAccountLove(account);
        statusLove1.setStatus(status);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(account);
        EasyMock.expect(statusDao.findOne(1L)).andReturn(status);
        EasyMock.expect(statusLoveDao.findByStatusAndAccountLove(status, account)).andReturn(statusLove1);
        statusLoveDao.delete(statusLove1);
        EasyMock.expectLastCall();
        EasyMock.expect(statusDao.save(status1)).andReturn(status1);

        control.replay();
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setAccountDao(accountDao).
                setStatusDao(statusDao).
                setStatusLoveDao(statusLoveDao);

        statusService.unlikeStatus(1, 1);
        Assert.assertEquals(1, statusLove1.getId());
    }

    @Test
    public void testListAllCategory()throws BizException{
        StatusCategoryDao statusCategoryDao = EasyMock.createMock(StatusCategoryDao.class);

        StatusCategory statusCategory1 = new StatusCategory();
        StatusCategory statusCategory2 = new StatusCategory();
        StatusCategory statusCategory3 = new StatusCategory();
        StatusCategory statusCategory4 = new StatusCategory();
        statusCategory1.setId(1);
        statusCategory2.setId(2);
        statusCategory3.setId(3);
        statusCategory4.setId(4);
        statusCategory1.setTitle("category1");
        statusCategory2.setTitle("category2");
        statusCategory3.setTitle("category3");
        statusCategory4.setTitle("category4");
        statusCategory1.setStatus_num(1);
        statusCategory2.setStatus_num(1);
        statusCategory3.setStatus_num(1);
        statusCategory4.setStatus_num(1);
        statusCategory1.setImg_url("www.alibaba.com");
        statusCategory2.setImg_url("www.alibaba.com");
        statusCategory3.setImg_url("www.alibaba.com");
        statusCategory4.setImg_url("www.alibaba.com");

        List<StatusCategory> list = new LinkedList<>();
        list.add(statusCategory1);
        list.add(statusCategory2);
        list.add(statusCategory3);
        list.add(statusCategory4);

        EasyMock.expect(statusCategoryDao.findAll()).andReturn(list);
        EasyMock.replay(statusCategoryDao);
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setStatusCategoryDao(statusCategoryDao);

        List<StatusCategoryDTO> l = statusService.listAllCategory();

        Assert.assertEquals(1 , l.get(0).getCategoryId());
    }

    @Test
    public void testListCommentByStatus() throws BizException{
        IMocksControl control = EasyMock.createControl();

        StatusDao statusDao = control.createMock(StatusDao.class);
        StatusCommentDao statusCommentDao = control.createMock(StatusCommentDao.class);
        AccountDetailDao accountDetailDao = control.createMock(AccountDetailDao.class);

        Status status = new Status();
        status.setId(1);
        //status.setComment_num(4);
        //status.setText("ddd");

        Account account1 = new Account();
        Account account2 = new Account();
        Account account3 = new Account();
        Account account4 = new Account();
        account1.setId(1);
        account2.setId(2);
        account3.setId(3);
        account4.setId(4);
        account1.setUsername("user1");
        account2.setUsername("user2");
        account3.setUsername("user3");
        account4.setUsername("user4");
        account1.setPassword(MD5Util.encoding("888"));
        account2.setPassword(MD5Util.encoding("888"));
        account3.setPassword(MD5Util.encoding("888"));
        account4.setPassword(MD5Util.encoding("888"));

        AccountDetail accountDetail1 = new AccountDetail();
        AccountDetail accountDetail2 = new AccountDetail();
        AccountDetail accountDetail3 = new AccountDetail();
        AccountDetail accountDetail4 = new AccountDetail();
        accountDetail1.setId(1);
        accountDetail2.setId(2);
        accountDetail3.setId(3);
        accountDetail4.setId(4);
        accountDetail1.setAccount(account1);
        accountDetail2.setAccount(account2);
        accountDetail3.setAccount(account3);
        accountDetail4.setAccount(account4);

        StatusComment statusComment1 = new StatusComment();
        StatusComment statusComment2 = new StatusComment();
        StatusComment statusComment3 = new StatusComment();
        StatusComment statusComment4 = new StatusComment();
        statusComment1.setId(1);
        statusComment2.setId(2);
        statusComment3.setId(3);
        statusComment4.setId(4);
        statusComment1.setContent("comment1");
        statusComment2.setContent("comment2");
        statusComment3.setContent("comment3");
        statusComment4.setContent("comment4");
        statusComment1.setStatus(status);
        statusComment2.setStatus(status);
        statusComment3.setStatus(status);
        statusComment4.setStatus(status);
        statusComment1.setCommentPublisher(account1);
        statusComment2.setCommentPublisher(account2);
        statusComment3.setCommentPublisher(account3);
        statusComment4.setCommentPublisher(account4);

        List<StatusComment> list = new LinkedList<>();
        list.add(statusComment1);
        list.add(statusComment2);
        list.add(statusComment3);
        list.add(statusComment4);

        EasyMock.expect(statusDao.findOne(1L)).andReturn(status);
        EasyMock.expect(statusCommentDao.findByStatus(status)).andReturn(list);
        EasyMock.expect(accountDetailDao.findByAccount(account1)).andReturn(accountDetail1);
        EasyMock.expect(accountDetailDao.findByAccount(account2)).andReturn(accountDetail2);
        EasyMock.expect(accountDetailDao.findByAccount(account3)).andReturn(accountDetail3);
        EasyMock.expect(accountDetailDao.findByAccount(account4)).andReturn(accountDetail4);

        control.replay();
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setStatusDao(statusDao).
                setStatusCommentDao(statusCommentDao).
                setAccountDetailDao(accountDetailDao);

        List<CommentDTO> l = statusService.listCommentByStatus(1);

        Assert.assertEquals(1, l.get(0).getPublisher().getUserId());
    }

    @Test
    public void testAddCollection() throws BizException{
        IMocksControl control = EasyMock.createControl();
        AccountDao accountDao = control.createMock(AccountDao.class);
        StatusDao statusDao = control.createMock(StatusDao.class);
        StatusCollectDao statusCollectDao = control.createMock(StatusCollectDao.class);

        Account account = new Account();
        account.setId(1);
        //account.setUsername("user");
        //account.setPassword(MD5Util.encoding("888"));

        Status status = new Status();
        status.setId(1);

        StatusCollect statusCollect1 = new StatusCollect();
        StatusCollect statusCollect2 = new StatusCollect();
        //statusCollect2.setId(1);
        statusCollect2.setId(2);
        statusCollect1.setAccountCollect(account);
        statusCollect2.setAccountCollect(account);
        statusCollect1.setStatus(status);
        statusCollect2.setStatus(status);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(account);
        //EasyMock.expect(statusDao.findOne(1L)).andReturn(status);
        EasyMock.expect(statusCollectDao.save(statusCollect1)).andReturn(statusCollect2);

        control.replay();
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setAccountDao(accountDao).
                setStatusDao(statusDao).
                setStatusCollectDao(statusCollectDao);

        statusService.addCollection(1, 1);
        Assert.assertEquals(2, statusCollect2.getId());
    }

    @Test
    public void testRemoveCollection() throws BizException{
        IMocksControl control = EasyMock.createControl();
        AccountDao accountDao = control.createMock(AccountDao.class);
        StatusDao statusDao = control.createMock(StatusDao.class);
        StatusCollectDao statusCollectDao = control.createMock(StatusCollectDao.class);

        Account account = new Account();
        account.setId(1);
        //account.setUsername("user");
        //account.setPassword(MD5Util.encoding("888"));

        Status status = new Status();
        status.setId(1);

        StatusCollect statusCollect1 = new StatusCollect();
        statusCollect1.setId(1);
        statusCollect1.setAccountCollect(account);
        statusCollect1.setStatus(status);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(account);
        //EasyMock.expect(statusDao.findOne(1L)).andReturn(status);
        EasyMock.expect(statusCollectDao.findByStatusAndAccountCollect(status, account)).andReturn(statusCollect1);
        statusCollectDao.delete(statusCollect1);
        EasyMock.expectLastCall();

        control.replay();
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setAccountDao(accountDao).
                setStatusDao(statusDao).
                setStatusCollectDao(statusCollectDao);

        statusService.removeCollection(1, 1);
        Assert.assertEquals(1, statusCollect1.getId());
    }

    @Test
    public void testGetStatusById() throws BizException{
        StatusDao statusDao = EasyMock.createMock(StatusDao.class);
        AccountDetailDao accountDetailDao = EasyMock.createMock(AccountDetailDao.class);
        StatusImageDao statusImageDao = EasyMock.createMock(StatusImageDao.class);

        Account account = new Account();
        account.setId(1);
        account.setUsername("user");
        account.setPassword(MD5Util.encoding("888"));

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setId(1);
        accountDetail.setAccount(account);
        accountDetail.setNickname("zhangsan");

        StatusCategory statusCategory2 = new StatusCategory();
        statusCategory2.setId(1);
        statusCategory2.setImg_url("www.sino.com");
        statusCategory2.setTitle("农家");

        Status status = new Status();
        status.setId(1);
        status.setStatusPublisher(account);
        status.setStatusCategory(statusCategory2);

        StatusImage statusImage1 = new StatusImage();
        StatusImage statusImage2 = new StatusImage();
        StatusImage statusImage3 = new StatusImage();
        StatusImage statusImage4 = new StatusImage();
        statusImage1.setId(1);
        statusImage2.setId(2);
        statusImage3.setId(3);
        statusImage4.setId(4);
        statusImage1.setStatus(status);
        statusImage2.setStatus(status);
        statusImage3.setStatus(status);
        statusImage4.setStatus(status);
        statusImage1.setQuality(1);
        statusImage2.setQuality(1);
        statusImage3.setQuality(1);
        statusImage4.setQuality(1);
        statusImage1.setUrl("pangnongfu");
        statusImage2.setUrl("pangnongfu");
        statusImage3.setUrl("pangnongfu");
        statusImage4.setUrl("pangnongfu");
        List<StatusImage> list = new LinkedList<>();
        list.add(statusImage1);
        list.add(statusImage2);
        list.add(statusImage3);
        list.add(statusImage4);

        EasyMock.expect(statusDao.findOne(1L)).andReturn(status);
        EasyMock.expect(accountDetailDao.findByAccount(account)).andReturn(accountDetail);
        EasyMock.expect(statusImageDao.findByStatus(status)).andReturn(list);

        EasyMock.replay(statusDao);
        EasyMock.replay(accountDetailDao);
        EasyMock.replay(statusImageDao);

        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setAccountDetailDao(accountDetailDao).
                setStatusDao(statusDao).
                setStatusImageDao(statusImageDao);

        StatusDTO statusDTO = statusService.getStatusById(1);

        Assert.assertEquals("zhangsan",statusDTO.getPublisherId().getNickname());
    }

    @Test
    public void testIsLikeStatus() throws BizException{
        IMocksControl control = EasyMock.createControl();
        AccountDao accountDao = control.createMock(AccountDao.class);
        StatusDao statusDao = control.createMock(StatusDao.class);
        StatusLoveDao statusLoveDao = control.createMock(StatusLoveDao.class);

        Account account = new Account();
        account.setId(1);
        //account.setUsername("user");
        //account.setPassword(MD5Util.encoding("888"));

        Status status = new Status();
        status.setId(1);

        StatusLove statusLove = new StatusLove();
        statusLove.setId(1);
        statusLove.setStatus(status);
        statusLove.setAccountLove(account);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(account);
        //EasyMock.expect(statusDao.findOne(1L)).andReturn(status);
        EasyMock.expect(statusLoveDao.findByStatusAndAccountLove(status, account)).andReturn(statusLove);

        control.replay();
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setStatusDao(statusDao).
                setAccountDao(accountDao).
                setStatusLoveDao(statusLoveDao);
        boolean flag = statusService.isLikeStatus(1, 1);

        Assert.assertTrue(flag);
    }

    @Test
    public void testIsCollected()throws BizException{
        IMocksControl control = EasyMock.createControl();
        AccountDao accountDao = control.createMock(AccountDao.class);
        StatusDao statusDao = control.createMock(StatusDao.class);
        StatusCollectDao statusCollectDao = control.createMock(StatusCollectDao.class);

        Account account = new Account();
        account.setId(1);
        //account.setUsername("user");
        //account.setPassword(MD5Util.encoding("888"));

        Status status = new Status();
        status.setId(1);

        StatusCollect statusCollect = new StatusCollect();
        statusCollect.setId(1);
        statusCollect.setStatus(status);
        statusCollect.setAccountCollect(account);

        //EasyMock.expect(accountDao.findOne(1L)).andReturn(account);
        //EasyMock.expect(statusDao.findOne(1L)).andReturn(status);
        EasyMock.expect(statusCollectDao.findByStatusAndAccountCollect(status, account)).andReturn(statusCollect);

        control.replay();
        EasyMock.verify();

        StatusService statusService = new StatusServiceImpl().
                setStatusDao(statusDao).
                setAccountDao(accountDao).
                setStatusCollectDao(statusCollectDao);
        boolean flag = statusService.isCollected(1, 1);

        Assert.assertTrue(flag);
    }
}
