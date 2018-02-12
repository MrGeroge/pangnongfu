package com.pangnongfu.server.dal;

import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.AccountDetailDao;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.AccountDetail;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

/**
 * Created by xinghai on 2015/9/30.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:test-daos.xml")
public class AccountDetailDaoTest  {
    private static final Logger logger= LoggerFactory.getLogger(AccountDetailDaoTest.class);
    private JdbcTemplate jdbcTemplate;
    private AccountDetailDao dao;
    private AccountDao ad;
    private long testUserId;
    @Before
    public void prepareData(){
        ApplicationContext context=new ClassPathXmlApplicationContext("test-daos.xml");
        jdbcTemplate=(JdbcTemplate)context.getBean("jdbcTemplate");
        dao=(AccountDetailDao)context.getBean("accountDetailDao");
        ad=(AccountDao)context.getBean("accountDao");
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(dao);
        Assert.assertNotNull(ad);
        final String sql1="INSERT INTO tb_account (username, password)  VALUES ('caiqi','123')";
        final String sql2="INSERT INTO tb_account_detail (phone,nickname,avatar_url,gender,city,district,about,account_id) VALUES ('18202790149','xinghai','www.baidu.com',0,'wuhan','hao','hao',1)";
        jdbcTemplate.execute(sql1);
        jdbcTemplate.execute(sql2);
    }
    @Test
    public void testFindAll(){
        Assert.assertEquals(1, dao.findAll().size());
    }
    @Test
    public void testFindOne(){
        AccountDetail accountDetail=new AccountDetail();
        accountDetail=dao.findOne((long)1 );
        Assert.assertNotNull(accountDetail);
        Assert.assertEquals(1,accountDetail.getId());
    }
    @Test
    public void testFindByAccount(){
        Account account=new Account();
        AccountDetail accountDetail=new AccountDetail();
        account=ad.findOne((long) 1);
        accountDetail=dao.findByAccount(account);
        Assert.assertEquals(1,accountDetail.getId());
    }
    @Test
    public void testAdd(){
        Account account=new Account();
        AccountDetail accountDetail=new AccountDetail();
        account.setUsername("ckey");
        account.setPassword("123");
        ad.save(account);
        accountDetail.setAbout("hao");
        accountDetail.setAvatar_url("www.baidu.com");
        accountDetail.setGender(0);
        accountDetail.setCity("wuhan");
        accountDetail.setAccount(account);
        accountDetail.setNickname("angle");
        accountDetail.setDistrict("hao");
        accountDetail.setPhone("15871832888");
        dao.save(accountDetail);
        Assert.assertEquals(2,dao.findAll().size());
    }
    @Test
    public void testDelete(){
        dao.delete((long) 1);
        Assert.assertEquals(0,dao.findAll().size());
    }
    @Test
    public void testUpdate(){
        AccountDetail accountDetail=new AccountDetail();
        accountDetail=dao.findOne((long) 1);
        accountDetail.setNickname("haowan");
        dao.save(accountDetail);
        Assert.assertEquals(1,dao.findAll().size());
    }

}
