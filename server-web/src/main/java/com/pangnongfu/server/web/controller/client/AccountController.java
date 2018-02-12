package com.pangnongfu.server.web.controller.client;

import com.pangnongfu.server.bal.api.AccountService;
import com.pangnongfu.server.bal.dto.AccountDetailDTO;
import com.pangnongfu.server.bal.exception.BizException;
import com.pangnongfu.server.web.utils.MD5Util;
import com.pangnongfu.server.web.vo.CommonResult;
import com.pangnongfu.server.web.vo.LoginResult;
import com.pangnongfu.server.web.vo.UserDetailVO;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Author:zhangyu
 * create on 15/9/19.
 */
@RestController
@RequestMapping("/account")
public class AccountController extends BaseController{

    private static Logger logger= LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码,需要Base64加密
     *
     * @return  登陆失败 {'status':'failed','message':'error message'}
     * 登陆成功 {'status':'success',
     *          'message':'success message',
     *          'token':'',
     *          'userDetail':{
     *             'userId':'',
     *             'nickname':'',
     *             'avatarUrl':'',
     *             'gender':'male/female',
     *             'city':'',
     *             'district':'',
     *             'about':''
     *          }}
     *
     * */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public LoginResult login(@RequestParam(value="username",required=true)String username,
                             @RequestParam(value="password",required=true)String password
                             ){
        /*
        * 1.base64解密
        * 2.调用AccountService中的validate
        * 3.token生成 MD5(yyyyDDMMhhmmss+Random)
        * 4.调用cache，key是token，value是UserDetail
        * 5.处理异常
        * */
        Base64 base64 = new Base64();
        byte[] debytes = null;//用于解码的byte数组
        String decodeStr = null;
        AccountDetailDTO detailDTO=null;//登陆后的用户detail信息
        String token=null;
        UserDetailVO detailVO;
        Date date ;//用来产生token的时间
        Random random;//用于产生随机数

        debytes = base64.decode(password);//解码
        decodeStr = new String(debytes);//将解码的byte数组整理为字符串

        logger.info(String.format("login username= %s,password= %s",username,decodeStr));
        try {
            detailDTO = accountService.validate(username,decodeStr);
        } catch (BizException e) {
            logger.error(String.format("login username= %s,Error",username), e);//若不成功则打印错误信息
           if(e.getErrorCode()==BizException.ERROR_CODE_PASSWORD_ERROR) {
               return new LoginResult().setStatus(LoginResult.FAILED_STATUS)
                       .setMessage("没有此用户");//返回
           }
           else if(e.getErrorCode()==BizException.ERROR_CODE_PASSWORD_ERROR){
                    return new LoginResult().setStatus(LoginResult.FAILED_STATUS)
                            .setMessage("密码错误");//返回
            }
           else{
                return new LoginResult().setStatus(LoginResult.FAILED_STATUS)
                        .setMessage("未知错误");//返回
            }
        }
        detailVO=new UserDetailVO();
        detailVO.setAbout(detailDTO.getAbout());
        detailVO.setAvatarUrl(detailDTO.getAvatarUrl());
        detailVO.setCity(detailDTO.getCity());
        detailVO.setDistrict(detailDTO.getDistrict());
        detailVO.setGender(detailDTO.getGender());
        detailVO.setNickname(detailDTO.getNickname());
        detailVO.setUserId(detailDTO.getUserId());//将dto中的数据set到vo中，

        date = new Date();
        random = new Random();
        token= MD5Util.encoding(date.toString()+random.nextInt(1000));//根据当前时间和一个随机数产生唯一的token

        logger.info(String.format("token=%s, login success",token));//打印登陆成功信息
        this.cache(token,detailVO);//保存token信息
        return new LoginResult().setStatus(LoginResult.SUCCESS_STATUS)
                .setMessage("登陆成功").setToken(token).setUserDetail(detailVO);//返回结果
    }

    /**
     * 用户注销
     *
     * @param token
     *
     * @return {'status':'','message':''}
     * */
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public CommonResult logout(@RequestParam(value="token",required=true)String token){
        logger.info(String.format("logout token=%s",token));

        this.removeCache(token);
        return new CommonResult()
                .setStatus(CommonResult.SUCCESS_STATUS)
                .setMessage("注销成功");
    }

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码,需要Base64加密
     *
     * @return {'status':'','message':''}
     *
     * */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Object register(@RequestParam(value="username",required=true)String username,
                                 @RequestParam(value="password",required=true)String password){
        Base64 base64 = new Base64();
        byte[] debytes = null;//用于解码的byte数组
        String decodeStr = null;

        debytes = base64.decode(password);//解码
        decodeStr = new String(debytes);//将解码的byte数组整理为字符串

        Map<String,String> successResult=new HashMap<>();

        try {
            logger.info(String.format("register username=%s,password=%s", username, decodeStr));
            long userId=accountService.addAccount(username,decodeStr);

            successResult.put("status",CommonResult.SUCCESS_STATUS);
            successResult.put("message","注册成功，请返回登陆");
            successResult.put("userId",String.valueOf(userId));

            return successResult;
        } catch (BizException e) {
            logger.error(String.format("regist error username=%s", username), e);//若不成功则打印错误信息
            if (e.getErrorCode()==BizException.ERROR_CODE_USERNAME_EXISTED){
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS)
                            .setMessage("用户已存在");//因为addaccount方法只有一个error抛出，本可以不用switch 但为了后期添加方便，还是用了switch
            }else {
                successResult.put("status", CommonResult.FAILED_STATUS);
                successResult.put("message", "服务器未知异常");
                return successResult;
            }
        }
    }

    /**
     * 更新密码/找回密码
     *
     * @param username 用户名
     * @param newPassword 新密码,需要Base64加密
     *
     * @return {'status':'','message':''}
     * */
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST)
    public CommonResult updatePassword(
            @RequestParam(value="username",required=true)String username,
            @RequestParam(value="newPassword",required=true)String newPassword){
        Base64 base64 = new Base64();
        byte[] debytes = null;//用于解码的byte数组
        String decodeStr = null;

        debytes = base64.decode(newPassword);//解码
        decodeStr = new String(debytes);//将解码的byte数组整理为字符串
        try {
            logger.info(String.format("updatePassword username=%s,password=%s",username,newPassword));
            accountService.updatePassword(username,decodeStr);
        } catch (BizException e) {
            logger.error(String.format("updatePassError usrname=%s",username), e);//若不成功则打印错误信息
            if(e.getErrorCode()==BizException.ERROR_CODE_USER_NOT_FOUND){
                    return new CommonResult().setStatus(CommonResult.FAILED_STATUS)
                            .setMessage("用户不存在");
            }else
            {
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS)
                        .setMessage("服务器未知错误");

            }
        }
        return new CommonResult()
                .setStatus(CommonResult.SUCCESS_STATUS)
                .setMessage("更换密码成功");
    }

    /**
     * 更新用户资料，需要token
     *
     * @param payload 需要更新的基本资料
     *
     * @return {'status':'','message':''}
     * */
    @RequestMapping(value = "/updateInfo",method = RequestMethod.POST)
    public CommonResult updateAccountInfo(@RequestBody String payload){
        logger.info(String.format("updateAccountInfo %s",payload));

        UserDetailVO detailVO;
        AccountDetailDTO detailDTO;

        detailVO=this.validateAndGetUser();
        detailDTO = new AccountDetailDTO();
        detailDTO.setUserId(detailVO.getUserId());

        try {
            JSONObject jsonObj=new JSONObject(payload);

            detailDTO.setNickname(jsonObj.getString("nickname"));
            detailDTO.setGender(jsonObj.getString("gender"));
            detailDTO.setAbout(jsonObj.getString("about"));
            detailDTO.setAvatarUrl(jsonObj.getString("avatarUrl"));
            detailDTO.setCity(jsonObj.getString("city"));
            detailDTO.setDistrict(jsonObj.getString("district"));//将vo中的数据set到dto中，以便更新

            accountService.updateAccountInfo(detailDTO);

            return new CommonResult()
                    .setStatus(CommonResult.SUCCESS_STATUS)
                    .setMessage("更新成功");
        } catch (BizException e) {
            logger.error(String.format("updateInfoError userId=%s",detailVO.getUserId()), e);//若不成功则打印错误信息

            return new CommonResult().setStatus(CommonResult.FAILED_STATUS)
                    .setMessage(e.getMessage());
        } catch (JSONException e){
            logger.error(String.format("updateInfoError userId=%s",detailVO.getUserId()), e);

            return new CommonResult().setStatus(CommonResult.FAILED_STATUS)
                    .setMessage(e.getMessage());
        }
    }
}
