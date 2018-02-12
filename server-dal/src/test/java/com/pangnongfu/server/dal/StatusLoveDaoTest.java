package com.pangnongfu.server.dal;

import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.StatusCollectDao;
import com.pangnongfu.server.dal.api.StatusDao;
import com.pangnongfu.server.dal.api.StatusLoveDao;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Status;
import com.pangnongfu.server.dal.po.StatusCollect;
import com.pangnongfu.server.dal.po.StatusLove;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinghai on 2015/10/1.
 */
public class StatusLoveDaoTest {
    private static final Logger logger= LoggerFactory.getLogger(StatusLoveDaoTest.class);
    private JdbcTemplate jdbcTemplate;
    private StatusLoveDao dao;
    private AccountDao ad;
    private StatusDao sd;

    @Before
    public void prepareData(){
        ApplicationContext context=new ClassPathXmlApplicationContext("test-daos.xml");
        jdbcTemplate=(JdbcTemplate)context.getBean("jdbcTemplate");
        dao=(StatusLoveDao)context.getBean("statusLoveDao");
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
        final String sql5="INSERT INTO tb_status_love (account_id,status_id) VALUES (1,1)";
        final String sql6="INSERT INTO tb_status_love (account_id,status_id) VALUES (2,1)";
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
    public void testFindByAccountLove(){
        Account account=new Account();
        List<StatusLove> statusLoveList=new ArrayList<StatusLove>();
        account=ad.findOne((long) 1);
        statusLoveList=dao.findByAccountLove(account);
        Assert.assertEquals(1,statusLoveList.size());
    }

    @Test
    public void testFindByStatusAndAccountLove(){
        Account account=new Account();
        Status status=new Status();
        StatusLove statusLove=new StatusLove();
        account=ad.findOne((long) 1);
        status=sd.findOne((long) 1);
        statusLove=dao.findByStatusAndAccountLove(status, account);
        Assert.assertNotNull(statusLove);
        Assert.assertEquals(1,statusLove.getId());
    }
    @Test
    public void testFindOne(){
        StatusLove statusLove=new StatusLove();
        statusLove=dao.findOne((long) 1);
        Assert.assertNotNull(statusLove);
        Assert.assertEquals(1,statusLove.getId());
    }

    @Test
    public void testAdd(){
        StatusLove statusLove=new StatusLove();
        Account account=new Account();
        account=ad.findOne((long) 2);
        statusLove.setAccountLove(account);
        Assert.assertEquals(2, dao.findAll().size());
    }

    @Test
    public void testDelete(){
        dao.delete((long) 1);
        Assert.assertEquals(1,dao.findAll().size());
    }

    @Test
    public void testUpdate(){
        StatusLove statusLove=new StatusLove();
        Account account=new Account();
        account=ad.findOne((long) 1);
        statusLove=dao.findOne((long) 2);
        statusLove.setAccountLove(account);
        dao.save(statusLove);
        Assert.assertEquals(2,dao.findAll().size());
    }

    @Test
    public void testDeleteStatusAndLove(){
        sd.delete((long) 1);
        Assert.assertEquals(0,dao.findAll().size());
    }
}
