package com.pangnongfu.server.dal;

import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.StatusCategoryDao;
import com.pangnongfu.server.dal.api.StatusCommentDao;
import com.pangnongfu.server.dal.api.StatusDao;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Status;
import com.pangnongfu.server.dal.po.StatusComment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xinghai on 2015/10/1.
 */
public class StatusCommentDaoTest {
    private static final Logger logger= LoggerFactory.getLogger(StatusCommentDaoTest.class);
    private JdbcTemplate jdbcTemplate;
    private StatusCommentDao dao;
    private StatusDao sd;
    private AccountDao ad;
    @Before
    public void prepareData(){
        ApplicationContext context=new ClassPathXmlApplicationContext("test-daos.xml");
        jdbcTemplate=(JdbcTemplate)context.getBean("jdbcTemplate");
        dao=(StatusCommentDao)context.getBean("statusCommentDao");
        ad=(AccountDao)context.getBean("accountDao");
        sd=(StatusDao)context.getBean("statusDao");
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(dao);
        Assert.assertNotNull(ad);
        Assert.assertNotNull(sd);
        final String sql1="INSERT INTO tb_account (username, password)  VALUES ('caiqi','123')";
        final String sql2="INSERT INTO tb_account (username, password)  VALUES  ('ckey', '123')";
        final String sql3="INSERT INTO tb_status_category (img_url,title,status_num) VALUES ('www.baidu.com','hao',0)";
        final String sql4="INSERT INTO tb_status (created_at,text,comment_num,love_num,publisher_id,category_id,longitude,latitude,address) VALUES ('2015-9-20 14:22:11','hao',0,0,1,1,40.69847032728747,73.9514422416687,'niuyue')";
        final String sql5="INSERT INTO tb_status_comment(create_at,content,publisher_id,status_id) VALUES ('2015-9-20 14:22:11','hao',1,1)";
        jdbcTemplate.execute(sql1);
        jdbcTemplate.execute(sql2);
        jdbcTemplate.execute(sql3);
        jdbcTemplate.execute(sql4);
        jdbcTemplate.execute(sql5);
    }

    @Test
    public void testFindAll(){
       Assert.assertEquals(1,dao.findAll().size());
    }

    @Test
    public void testFindByCommentPublisher(){
        Account publisher=new Account();
        List<StatusComment> statusCommentList=new ArrayList<StatusComment>();
        publisher=ad.findOne((long) 1);
        statusCommentList=dao.findByCommentPublisher(publisher);
        Assert.assertEquals(1,statusCommentList.size());
    }

    @Test
    public void testFindByStatus(){
        Status status=new Status();
        List<StatusComment> statusCommentList=new ArrayList<StatusComment>();
        status=sd.findOne((long) 1);
        statusCommentList=dao.findByStatus(status);
        Assert.assertEquals(1,statusCommentList.size());
    }

    @Test
    public void testFindOne(){
        StatusComment statusComment=new StatusComment();
        statusComment=dao.findOne((long) 1);
        Assert.assertNotNull(statusComment);
        Assert.assertEquals(1,statusComment.getId());
    }

    @Test
    public void testAdd(){
        Account publisher=new Account();
        publisher=ad.findOne((long) 2);
        StatusComment statusComment=new StatusComment();
        statusComment.setCreate_at(new Date());
        statusComment.setContent("hao");
        statusComment.setCommentPublisher(publisher);
        dao.save(statusComment);
        Assert.assertEquals(2,dao.findAll().size());
    }

    @Test
    public void testDelete(){
        dao.delete((long) 1);
        Assert.assertEquals(0,dao.findAll().size());
    }

    @Test
    public void testUpdate(){
        StatusComment statusComment=new StatusComment();
        statusComment=dao.findOne((long) 1);
        statusComment.setCreate_at(new Date());
        dao.save(statusComment);
        Assert.assertEquals(1,dao.findAll().size());
    }

    @Test
    public void testDeleteStatusAndComment(){
       sd.delete((long) 1);
        Assert.assertEquals(0,dao.findAll().size());
    }
}

