package com.pangnongfu.server.web;

import com.pangnongfu.server.bal.api.AccountService;
import com.pangnongfu.server.bal.dto.AccountDetailDTO;
import com.pangnongfu.server.bal.exception.BizException;
import com.pangnongfu.server.web.controller.client.AccountController;
import com.pangnongfu.server.web.vo.CommonResult;
import com.pangnongfu.server.web.vo.LoginResult;
import com.pangnongfu.server.web.vo.UserDetailVO;
import org.apache.commons.codec.binary.Base64;
import org.easymock.EasyMock;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Author:zhangyu
 * create on 15/9/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:webContext.xml")
@WebAppConfiguration
public class AccountControllerMockTest extends AbstractJUnit4SpringContextTests {
    private static Logger logger= LoggerFactory.getLogger(AccountControllerMockTest.class);

    @Test
    public void testLoginAndLogout()  {
        AccountController accountController =new AccountController();
        AccountService accountService = EasyMock.createMock(AccountService.class);
        AccountDetailDTO detailDTO = new AccountDetailDTO();
        String encodeStr = null;

        detailDTO.setDistrict("districk");
        detailDTO.setCity("wuhan");
        detailDTO.setAvatarUrl("www.hehe.com/test.jpg");
        detailDTO.setAbout("what is about?");
        detailDTO.setGender("gender?what?");
        detailDTO.setNickname("shuiyu");
        detailDTO.setUserId(123);
        /**
         * 设置预期数据
         *
         * */
        try {
            EasyMock.expect(accountService.validate("shuiyu", "123")).andReturn(detailDTO);
            EasyMock.expect(accountService.validate("douyu", "123")).andThrow(new BizException(BizException.ERROR_CODE_USER_NOT_FOUND, "无此用户")).anyTimes();
            EasyMock.expect(accountService.validate("shuiyu", "456")).andThrow(new BizException(BizException.ERROR_CODE_PASSWORD_ERROR, "用户名密码错误")).anyTimes();
        } catch (BizException e) {
            e.printStackTrace();
        }

        EasyMock.replay(accountService);

        accountController.setAccountService(accountService);
        Assert.assertNotNull(accountController);
        encodeStr=Base64.encodeBase64String("123".getBytes());
        LoginResult result1=accountController.login("shuiyu",encodeStr,String.valueOf(111),String.valueOf(23));
        LoginResult result2 = accountController.login("shuiyu",Base64.encodeBase64String("456".getBytes()),String.valueOf(111),String.valueOf(23));
        LoginResult result3 = accountController.login("douyu",encodeStr,String.valueOf(111),String.valueOf(23));
        Assert.assertNotNull(result1);
        Assert.assertEquals(LoginResult.SUCCESS_STATUS, result1.getStatus());//测试登陆成功案例

        CommonResult commonResult = accountController.logout(result1.getToken());
        Assert.assertNotNull(commonResult);
        Assert.assertEquals(commonResult.SUCCESS_STATUS,commonResult.getStatus());//测试注销

        Assert.assertNotNull(result2);
        Assert.assertEquals(LoginResult.FAILED_STATUS,result3.getStatus());//测试产生无此用户异常

        Assert.assertNotNull(result3);
        Assert.assertEquals(LoginResult.FAILED_STATUS,result3.getStatus());//测试产生密码错误异常
    }
    @Test
    public void testLogout(){
        AccountController accountController =new AccountController();
        AccountService accountService = EasyMock.createMock(AccountService.class);

        try {
            EasyMock.expect(accountService.addAccount("shuiyu", "123")).andReturn((long) 1);
            EasyMock.expect(accountService.addAccount("douyu", "123")).andThrow(new BizException(BizException.ERROR_CODE_USERNAME_EXISTED, "该用户名已注册")).anyTimes();
        } catch (BizException e) {
            e.printStackTrace();
        }
        EasyMock.replay(accountService);
        accountController.setAccountService(accountService);

        CommonResult result1 = (CommonResult)accountController.register("shuiyu",Base64.encodeBase64String("123".getBytes()));
        CommonResult result2 = (CommonResult)accountController.register("douyu",Base64.encodeBase64String("123".getBytes()));

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertEquals(LoginResult.SUCCESS_STATUS, result1.getStatus());//测试成功案例
        Assert.assertEquals(LoginResult.FAILED_STATUS,result2.getStatus());//测试产生用户名重复异常
    }
    @Test
    public void testUpdatePass(){
        //并不知道该怎么模拟validateandgetuser那个方法的返回值，所以就直接用先登录上去，对于抛出异常的用例也是先模拟登陆上，但是在service层却模拟抛出异常
        AccountController accountController =EasyMock.createMock(AccountController.class);
        AccountService accountService = EasyMock.createMock(AccountService.class);
        try {
            accountService.updatePassword("shuiyu", "123");//这里并不能跑通，因为会会抛出异常，异常也就罢了，尼玛返回时void还抛出异常我就不造咋搞了。
            EasyMock.expectLastCall();
            accountService.updatePassword("douyu", "123");
            EasyMock.expectLastCall().andThrow(new BizException(BizException.ERROR_CODE_USER_NOT_FOUND, "无此用户")).anyTimes();
        } catch (BizException e) {
            e.printStackTrace();
        }
        EasyMock.replay(accountService);
        accountController.setAccountService(accountService);

        CommonResult result1 = accountController.updatePassword("shuiyu", Base64.encodeBase64String("123".getBytes()));
        CommonResult result2 = accountController.updatePassword("douyu", Base64.encodeBase64String("123".getBytes()));

        Assert.assertNotNull(result1);
        Assert.assertNotNull(result2);
        Assert.assertEquals(LoginResult.SUCCESS_STATUS, result1.getStatus());//测试成功案例
        Assert.assertEquals(LoginResult.FAILED_STATUS,result2.getStatus());//测试产生用户名重复异常
    }
}
