package com.pangnongfu.server.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by shuiyu on 2015/9/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:webContext.xml")
@WebAppConfiguration
public class StatusControllerMockTest {
    @Test
    public void testGetPublicList(){

    }
}
