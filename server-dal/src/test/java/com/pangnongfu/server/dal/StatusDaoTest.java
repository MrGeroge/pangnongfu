package com.pangnongfu.server.dal;

import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.StatusCategoryDao;
import com.pangnongfu.server.dal.api.StatusDao;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Status;
import com.pangnongfu.server.dal.po.StatusCategory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xinghai on 2015/10/1.
 */
public class StatusDaoTest {
    private static final Logger logger= LoggerFactory.getLogger(StatusDaoTest.class);
    private JdbcTemplate jdbcTemplate;
    private StatusDao dao;
    private StatusCategoryDao scd;
    private AccountDao ad;

    @Before
    public void prepareData(){
        ApplicationContext context=new ClassPathXmlApplicationContext("test-daos.xml");
        jdbcTemplate=(JdbcTemplate)context.getBean("jdbcTemplate");
        dao=(StatusDao)context.getBean("statusDao");
        ad=(AccountDao)context.getBean("accountDao");
        scd=(StatusCategoryDao)context.getBean("statusCategoryDao");
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(dao);
        Assert.assertNotNull(ad);
        Assert.assertNotNull(scd);
        final String sql1="INSERT INTO tb_account (username, password)  VALUES ('caiqi','123')";
        final String sql2="INSERT INTO tb_account (username, password)  VALUES  ('ckey', '123')";
        final String sql3="INSERT INTO tb_status_category (img_url,title,status_num) VALUES ('www.baidu.com','hao',0)";
        final String sql4="INSERT INTO tb_status (created_at,text,comment_num,love_num,publisher_id,category_id,longitude,latitude,address) VALUES ('2015-9-20 14:22:11','hao',0,0,1,1,40.69847032728747,73.9514422416687,'niuyue')";
        jdbcTemplate.execute(sql1);
        jdbcTemplate.execute(sql2);
        jdbcTemplate.execute(sql3);
        jdbcTemplate.execute(sql4);
    }

    @Test
    public void testFindAll(){
        Assert.assertEquals(1,dao.findAll().size());
    }

    @Test
    public void testFindByStatusPublisher(){
     Account publisher=new Account();
        List<Status> statusList=new ArrayList<Status>();
        publisher=ad.findOne((long) 1);
        statusList=dao.findByStatusPublisher(publisher);
        Assert.assertEquals(1,statusList.size());
    }

    @Test
    public void testFindByStatusPublisherAndPageable(){
        Account publisher=new Account();
        publisher=ad.findOne((long) 1);
        Page<Status> statusPage=dao.findByStatusPublisher(publisher,new PageRequest(2,1));
        Assert.assertEquals(1,statusPage.getSize());
    }

    @Test
    public void testFindByStatusCategory(){
        StatusCategory statusCategory=new StatusCategory();
        statusCategory=scd.findOne((long) 1);
        Page<Status> statusPage=dao.findByStatusCategory(statusCategory, new PageRequest(2, 1));
        Assert.assertEquals(1,statusPage.getSize());
    }

    @Test
    public void testFindOne(){
        Status status=new Status();
        status=dao.findOne((long )1);
        Assert.assertNotNull(status);
        Assert.assertEquals(1,status.getId());
    }

    @Test
    public void testAdd(){
        Status status=new Status();
        Account publisher=new Account();
        publisher=ad.findOne((long )2);
        status.setCreated_at(new Date());
        status.setText("hao");
        status.setStatusPublisher(publisher);
        status.setLove_num(0);
        status.setComment_num(0);
        status.setAddress("wuhan");
        status.setLongitude(40.111111111111);
        status.setLatitude(20.123131313131);
        dao.save(status);
        Assert.assertEquals(2,dao.findAll().size());
    }

    @Test
    public void testDelete(){
        dao.delete((long )1);
        Assert.assertEquals(0,dao.findAll().size());
    }

    @Test
    public void testUpdate(){
        Status status=new Status();
        status=dao.findOne((long) 1);
        status.setText("abc");
        dao.save(status);
        Assert.assertEquals(1,dao.findAll().size());
    }
}
