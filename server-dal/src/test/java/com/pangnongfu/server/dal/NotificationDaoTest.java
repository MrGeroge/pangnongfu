package com.pangnongfu.server.dal;

import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.NotificationDao;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Notification;
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
 * Created by xinghai on 2015/9/30.
 */
public class NotificationDaoTest {
    private static final Logger logger= LoggerFactory.getLogger(NotificationDaoTest.class);
    private JdbcTemplate jdbcTemplate;
    private NotificationDao dao;
    private AccountDao ad;

    @Before
    public void prepareData(){
        ApplicationContext context=new ClassPathXmlApplicationContext("test-daos.xml");
        jdbcTemplate=(JdbcTemplate)context.getBean("jdbcTemplate");
        dao=(NotificationDao)context.getBean("notificationDao");
        ad=(AccountDao)context.getBean("accountDao");
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(dao);
        Assert.assertNotNull(ad);
        final String sql1="INSERT INTO tb_account (username, password)  VALUES ('caiqi','123')";
        final String sql2="INSERT INTO tb_account(username, password)  VALUES('ckey', '123')";
        final String sql3="INSERT  INTO tb_notification(post_status,create_at,content,producer_id,consumer_id) VALUES(0,'2015-9-24 14:22:11','hao',1,2);";
        jdbcTemplate.execute(sql1);
        jdbcTemplate.execute(sql2);
        jdbcTemplate.execute(sql3);
    }

    @Test
    public void testFindAll(){
        Assert.assertEquals(1,dao.findAll().size());
    }

    @Test
    public void testFindByConsumer(){
        Account account=new Account();
        List<Notification> notificationList=new ArrayList<Notification>();
        account=ad.findOne((long) 2);
        notificationList=dao.findByConsumer(account);
        Assert.assertEquals(1,notificationList.size());
    }

    @Test
    public void testFindOne(){
        Notification notification=new Notification();
        notification=dao.findOne((long) 1);
        Assert.assertNotNull(notification);
        Assert.assertEquals(1,notification.getId());
    }

    @Test
    public void testAdd(){
        Account consumer=new Account();
        Notification notification=new Notification();
        consumer=ad.findOne((long )1);
        notification.setPost_status(0);
        notification.setConsumer(consumer);
        notification.setContent("hao");
        notification.setCreate_at(new Date());
        notification.setProducer_id(2);
        dao.save(notification);
        Assert.assertEquals(2,dao.findAll().size());
    }

    @Test
    public void testDelete(){
        dao.delete((long) 1);
        Assert.assertEquals(0,dao.findAll().size());
    }

    @Test
    public void testUpdate(){
        Notification notification=new Notification();
        notification=dao.findOne((long) 1);
        notification.setPost_status(1);
        dao.save(notification);
        Assert.assertEquals(1,dao.findAll().size());
    }


}
