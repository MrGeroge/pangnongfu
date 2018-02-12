package com.pangnongfu.server.dal;

import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.StatusCategoryDao;
import com.pangnongfu.server.dal.api.StatusCollectDao;
import com.pangnongfu.server.dal.api.StatusDao;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Status;
import com.pangnongfu.server.dal.po.StatusCollect;
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
import java.util.List;

/**
 * Created by xinghai on 2015/10/1.
 */
public class StatusCollectDaoTest {
    private static final Logger logger= LoggerFactory.getLogger(StatusCollectDaoTest.class);
    private JdbcTemplate jdbcTemplate;
    private StatusCollectDao dao;
    private AccountDao ad;
    private StatusDao sd;

    @Before
    public void prepareData(){
        ApplicationContext context=new ClassPathXmlApplicationContext("test-daos.xml");
        jdbcTemplate=(JdbcTemplate)context.getBean("jdbcTemplate");
        dao=(StatusCollectDao)context.getBean("statusCollectDao");
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
        final String sql5="INSERT INTO tb_status_collect (account_id,status_id) VALUES (1,1)";
        final String sql6="INSERT INTO tb_status_collect (account_id,status_id) VALUES (2,1)";
        jdbcTemplate.execute(sql1);
        jdbcTemplate.execute(sql2);
        jdbcTemplate.execute(sql3);
        jdbcTemplate.execute(sql4);
        jdbcTemplate.execute(sql5);
        jdbcTemplate.execute(sql6);
    }
    @Test
    public void testFindAll(){
        Assert.assertEquals(2,dao.findAll().size());
    }

    @Test
    public void testFindByAccountCollect(){
        Account account=new Account();
        List<StatusCollect> statusCollectList=new ArrayList<StatusCollect>();
        account=ad.findOne((long) 1);
        statusCollectList=dao.findByAccountCollect(account);
        Assert.assertEquals(1,statusCollectList.size());
    }

    @Test
    public void testFindByAccountCollectAndPageable(){
        Account account=new Account();
        account=ad.findOne((long) 1);
        Page<StatusCollect> statusCollectPage=dao.findByAccountCollect(account,new PageRequest(2,1));
        Assert.assertEquals(1,statusCollectPage.getSize());
    }

    @Test
    public void testFindByStatusAndAccountCollect(){
        Account account=new Account();
        Status status=new Status();
        StatusCollect statusCollect=new StatusCollect();
        account=ad.findOne((long) 1);
        status=sd.findOne((long )1);
        statusCollect=dao.findByStatusAndAccountCollect(status, account);
        Assert.assertNotNull(statusCollect);
        Assert.assertEquals(1,statusCollect.getId());
    }
    @Test
    public void testFindOne(){
        StatusCollect statusCollect=new StatusCollect();
        statusCollect=dao.findOne((long) 1);
        Assert.assertNotNull(statusCollect);
        Assert.assertEquals(1,statusCollect.getId());
    }
    @Test
    public void testAdd(){
        StatusCollect statusCollect=new StatusCollect();
        Account account=new Account();
        account=ad.findOne((long) 2);
        statusCollect.setAccountCollect(account);
        Assert.assertEquals(2,dao.findAll().size());
    }

    @Test
    public void testDelete(){
        dao.delete((long) 1);
        Assert.assertEquals(1,dao.findAll().size());
    }

    @Test
    public void testUpdate(){
        StatusCollect statusCollect=new StatusCollect();
        Account account=new Account();
        account=ad.findOne((long) 1);
        statusCollect=dao.findOne((long) 2);
        statusCollect.setAccountCollect(account);
        dao.save(statusCollect);
        Assert.assertEquals(2,dao.findAll().size());
    }

    @Test
    public void testDeleteStatusAndCollect(){
        sd.delete((long) 1);
        Assert.assertEquals(0,dao.findAll().size());
    }

}

