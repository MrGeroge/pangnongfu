package com.pangnongfu.server.dal;

import com.pangnongfu.server.dal.api.AccountDao;
import com.pangnongfu.server.dal.api.StatusCategoryDao;
import com.pangnongfu.server.dal.api.StatusDao;
import com.pangnongfu.server.dal.api.StatusImageDao;
import com.pangnongfu.server.dal.po.StatusImage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by xinghai on 2015/10/1.
 */
public class StatusImageDaoTest {
    private static final Logger logger= LoggerFactory.getLogger(StatusImageDaoTest.class);
    private JdbcTemplate jdbcTemplate;
    private StatusImageDao dao;
    private StatusDao sd;

    @Before
    public void prepareData(){
        ApplicationContext context=new ClassPathXmlApplicationContext("test-daos.xml");
        jdbcTemplate=(JdbcTemplate)context.getBean("jdbcTemplate");
        dao=(StatusImageDao)context.getBean("statusImageDao");
        sd=(StatusDao)context.getBean("statusDao");
        Assert.assertNotNull(jdbcTemplate);
        Assert.assertNotNull(dao);
        Assert.assertNotNull(sd);
        final String sql1="INSERT INTO tb_account (username, password)  VALUES ('caiqi','123')";
        final String sql2="INSERT INTO tb_account (username, password)  VALUES  ('ckey', '123')";
        final String sql3="INSERT INTO tb_status_category (img_url,title,status_num) VALUES ('www.baidu.com','hao',0)";
        final String sql4="INSERT INTO tb_status (created_at,text,comment_num,love_num,publisher_id,category_id,longitude,latitude,address) VALUES ('2015-9-20 14:22:11','hao',0,0,1,1,40.69847032728747,73.9514422416687,'niuyue')";
        final String sql5="INSERT INTO tb_status_image (url,quality,status_id) VALUES ('www.caiqi.com',0,1)";
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
    public void testFindOne(){
        StatusImage statusImage=new StatusImage();
        statusImage=dao.findOne((long) 1);
        Assert.assertNotNull(statusImage);
        Assert.assertEquals(1,statusImage.getId());
    }

    @Test
    public void testAdd(){
        StatusImage statusImage=new StatusImage();
        statusImage.setUrl("www.souhu.com");
        statusImage.setQuality(0);
        dao.save(statusImage);
        Assert.assertEquals(2,dao.findAll().size());
    }

    @Test
    public void testDelete(){
        dao.delete((long) 1);
        Assert.assertEquals(0,dao.findAll().size());
    }

    @Test
    public void testUpdate(){
        StatusImage statusImage=new StatusImage();
        statusImage=dao.findOne((long) 1);
        statusImage.setQuality(1);
        dao.save(statusImage);
        Assert.assertEquals(1,dao.findAll().size());
    }

    @Test
    public void testDeleteStatusAndImage(){
        sd.delete((long) 1);
        Assert.assertEquals(0,dao.findAll().size());
    }

}
