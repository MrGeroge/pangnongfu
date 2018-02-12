package com.pangnongfu.server.web.controller.client;

import com.pangnongfu.server.bal.api.SocialService;
import com.pangnongfu.server.bal.dto.AccountSimpleDTO;
import com.pangnongfu.server.bal.dto.Pageable;
import com.pangnongfu.server.bal.exception.BizException;
import com.pangnongfu.server.web.endpoint.NotificationEndpoint;
import com.pangnongfu.server.web.vo.AccountSimpleVO;
import com.pangnongfu.server.web.vo.CommonResult;
import com.pangnongfu.server.web.vo.SocialResult;
import com.pangnongfu.server.web.vo.UserDetailVO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuiyu on 2015/9/28.
 */
@RestController
@RequestMapping("/social")
public class SocialController extends BaseController{
    private static Logger logger= LoggerFactory.getLogger(SocialController.class);
    private SocialService socialService;

    @Autowired
    public void setSocialService(SocialService socialService){this.socialService=socialService;}

    /**
     * 需要token进行
     * 添加关注
     * @param userId 要关注人的id
     * @return
     *  推送消息的格式
     * {
     *    'formAccount': {
     *          'userId':''
     *          'avatarUrl':''
     *          'nickname':''
     *     }
     *     'tag'
     * }
     */
    @RequestMapping(value="/follow/add",method= RequestMethod.POST)
    public CommonResult followUser(@RequestParam(value = "userId", required = true) long userId) {
        UserDetailVO userDetailVO = this.validateAndGetUser();
        long loginId=userDetailVO.getUserId();
        JSONObject jsonObject;
        logger.info(String.format("userId=%d followed userId=%d",loginId,userId));
        try {
            socialService.followUser(loginId, userId);
        } catch (BizException e) {
            logger.error(String.format("Error at following user  userId=%d followedId=%d", loginId,userId), e);
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER){
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("请求参数错误");
            }else if(e.getErrorCode()==BizException.ERROR_CODE_INSTANCE_NOT_FOUND){
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("账户或状态实例未找到！，请重试");
            }
        }

        jsonObject = new JSONObject();
        jsonObject.put("formAccount",new AccountSimpleVO(userDetailVO.getUserId(),userDetailVO.getNickname(),userDetailVO.getAvatarUrl()));
        jsonObject.put("tag", NotificationEndpoint.SEND_CODE_FOLLOW);
        NotificationEndpoint.sendMessage(userId,jsonObject.toString());
        return new CommonResult()
                .setStatus(CommonResult.SUCCESS_STATUS)
                .setMessage("关注成功");
    }

    /**
     * 取消关注
     * 需要token
     * @param userId 要取消关注的人
     * @return
     */
    @RequestMapping(value="/follow/delete",method= RequestMethod.POST)
    public CommonResult cancelFollow(@RequestParam(value="userId",required = true)long userId){
        long loginId=this.validateAndGetUser().getUserId();
        logger.info(String.format("userId=%d cancel follow userId=%d",loginId,userId));
        try {
            socialService.cancelFollowUser(loginId,userId);
        } catch (BizException e) {
            logger.error(String.format("Error at canceling follow user  userId=%d followedId=%d", loginId, userId), e);
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER){
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("请求参数错误");
            }else if(e.getErrorCode()==BizException.ERROR_CODE_INSTANCE_NOT_FOUND){
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("账户或状态实例未找到！，请重试");
            }
        }
        return new CommonResult()
                .setStatus(CommonResult.SUCCESS_STATUS)
                .setMessage("取消收藏");
    }

    /**
     * 用户得到关注自己的人 需要token
     * @param page 第几页
     * @param count 一页几个
     * @return
     * {
     *      'currentPage':'',
     *      'maxPage':'',
     *       'count':'',
     *       'allCount':'',
     *      [
     *          {   userId:"",
     *              nickname:"",
     *              avatarUrl:"",
     *          }
     *      ]
     *  }
     */
    @RequestMapping(value="/follower/list",method=RequestMethod.POST)
    public Object listMyFollower(@RequestParam(value="page",required=false,defaultValue = "1")int page,
                                 @RequestParam(value="count",required=false,defaultValue = "10")int count,
                                 @RequestParam(value="userId",required = true)long userId){
        SocialResult<AccountSimpleVO> followers ;
        List<AccountSimpleVO> accounts;
        Pageable<AccountSimpleDTO>accountDTOs = null;

        accounts = new ArrayList<>();

        logger.info(String.format("userId=%d,list his followers",userId));

        followers = new SocialResult<>();
        followers.setHasNextPage(accountDTOs.getHasNextPage());
        followers.setHasPrePage(accountDTOs.getHasPrePage());
        followers.setCurrentPage(accountDTOs.getCurrentPage());
        for(AccountSimpleDTO accountSimpleDTO:accountDTOs.getContent()){
            AccountSimpleVO account = new AccountSimpleVO();
            account.setNickname(accountSimpleDTO.getNickname());
            account.setAvatar_url(accountSimpleDTO.getAvatarUrl());
            account.setAccount_id(accountSimpleDTO.getUserId());
            accounts.add(account);
        }
        followers.setCount(accounts.size());
        followers.setContent(accounts);
        return followers;
    }

    /**
     * 查看我关注的人 需要token
     * @param page 第几页
     * @param count 一页几个
     * @return
     */
    @RequestMapping(value="/following/list",method=RequestMethod.POST)
    public  Object listWhoIFollowed(@RequestParam(value="page",required=false,defaultValue = "1")int page,
                                              @RequestParam(value="count",required=false,defaultValue = "10")int count){
        SocialResult<AccountSimpleVO> followers ;
        List<AccountSimpleVO> accounts;
        Pageable<AccountSimpleDTO>accountDTOs=null;
        long userId;

        userId=this.validateAndGetUser().getUserId();
        try {
            accountDTOs=socialService.listFollowing(userId, page, count);
        } catch (BizException e) {
            logger.error(String.format("Error at listing my following  userId=%d ", userId), e);
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER){
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("请求参数错误");
            }
        }
        accounts = new ArrayList<>();

        logger.info(String.format("userId=%d,list who he follows",userId));

        followers = new SocialResult<>();
        followers.setHasNextPage(accountDTOs.getHasNextPage());
        followers.setHasPrePage(accountDTOs.getHasPrePage());
        followers.setCurrentPage(accountDTOs.getCurrentPage());
        for(AccountSimpleDTO accountSimpleDTO:accountDTOs.getContent()){
            AccountSimpleVO account = new AccountSimpleVO();
            account.setNickname(accountSimpleDTO.getNickname());
            account.setAvatar_url(accountSimpleDTO.getAvatarUrl());
            account.setAccount_id(accountSimpleDTO.getUserId());
            accounts.add(account);
        }
        followers.setCount(accounts.size());
        followers.setContent(accounts);
        return followers;
    }
}
