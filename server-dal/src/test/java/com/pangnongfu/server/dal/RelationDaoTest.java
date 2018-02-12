package com.pangnongfu.server.dal;

import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.RelationDao;
import com.pangnongfu.server.dal.po.Account;
import com.pangnongfu.server.dal.po.Relation;
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
public class RelationDaoTest {
    private static final Logger logger= LoggerFactory.getLogger(RelationDaoTest.class);
    private JdbcTemplate jdbcTemplate;
    private RelationDao dao;
    private AccountDao ad;

    @Before
    public void prepareData(){
        ApplicationContext context=new ClassPathXmlApplicationContext("test-daos.xml");
        jdbcTemplate=(JdbcTemplate)context.getBean("jdbcTemplate");
        dao=(RelationDao)context.getBean("relationDao");
        ad=(AccountDao)context.getBean("accountDao");
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(dao);
        Assert.assertNotNull(ad);
        final String sql1="INSERT INTO tb_account (username, password)  VALUES ('caiqi','123')";
        final String sql2="INSERT INTO tb_account (username, password)  VALUES  ('ckey', '123')";
        final String sql3="INSERT INTO tb_account (username, password)  VALUES ('zy','123')";
        final String sql4="INSERT  INTO tb_relation (follower_id,following_id) VALUES (1,2)";
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
    public void testFindByFollower(){
        Account account=new Account();
        List<Relation> relationList=new ArrayList<Relation>();
        account=ad.findOne((long) 1);
        relationList=dao.findByFollower(account);
        Assert.assertEquals(1,relationList.size());
    }

    @Test
    public void testFindByFollowing(){
        Account account=new Account();
        List<Relation> relationList=new ArrayList<Relation>();
        account=ad.findOne((long) 2);
        relationList=dao.findByFollowing(account);
        Assert.assertEquals(1,relationList.size());
    }

    @Test
    public void testFindByFollowerAndPageable(){
        Account account=new Account();
        account=ad.findOne((long) 1);
        Page<Relation> relationPage=dao.findByFollower(account,new PageRequest(2,1));
        Assert.assertEquals(1,relationPage.getSize());
    }

    @Test
    public void testFindByFollowingAndPageable(){
        Account account=new Account();
        account=ad.findOne((long) 2);
        Page<Relation> relationPage=dao.findByFollowing(account, new PageRequest(2, 1));
        Assert.assertEquals(1,relationPage.getSize());
    }

    @Test
    public void testFindByFollowerAndFollowing(){
        Account follower=new Account();
        Account following=new Account();
        Relation relation=new Relation();
        follower=ad.findOne((long) 1);
        following=ad.findOne((long) 2);
        relation=dao.findByFollowerAndFollowing(follower, following);
        Assert.assertNotNull(relation);
        Assert.assertEquals(1,relation.getId());
    }

    @Test
    public void testAdd(){
        Account follower=new Account();
        Account following=new Account();
        Relation relation=new Relation();
        follower=ad.findOne((long) 2);
        following=ad.findOne((long) 1);
        relation.setFollower(follower);
        relation.setFollowing(following);
        dao.save(relation);
        Assert.assertEquals(2,dao.findAll().size());
    }

    @Test
    public void testDelete(){
        dao.delete((long) 1);
        Assert.assertEquals(0,dao.findAll().size());
    }

    @Test
    public void testUpdate(){
        Relation relation=new Relation();
        Account follower=new Account();
        follower=ad.findOne((long) 3);
        relation=dao.findOne((long) 1);
        relation.setFollower(follower);
        Assert.assertEquals(1,dao.findAll().size());
    }


}
