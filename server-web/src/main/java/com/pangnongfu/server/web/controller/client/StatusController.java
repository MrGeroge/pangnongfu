package com.pangnongfu.server.web.controller.client;

import com.pangnongfu.server.bal.api.SocialService;
import com.pangnongfu.server.bal.api.StatusService;
import com.pangnongfu.server.bal.dto.*;
import com.pangnongfu.server.bal.exception.BizException;
import com.pangnongfu.server.web.endpoint.NotificationEndpoint;
import com.pangnongfu.server.web.utils.DataTraslator;
import com.pangnongfu.server.web.vo.*;
import net.sf.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Author:zhangyu
 * create on 15/9/19.
 */
@RestController
@RequestMapping("/status")
public class StatusController extends BaseController{
    private static Logger logger= LoggerFactory.getLogger(StatusController.class);
    private StatusService statusService;
    private SocialService socialService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    public void setSocialService(SocialService socialService){this.socialService=socialService;}

    @Autowired
    public void setStatusService(StatusService statusService){this.statusService=statusService;}
    /**
     *
     *  获取公共列表，有token时候返回当前用户和发布者之间关系
     *  以及当前用户和状态关系
     *
     *
     *  @param page 页数
     *  @param count 每页数量
     *
     *
     *
     */
    @RequestMapping(value = "/public",method = RequestMethod.POST)
    public Object  listPublicStatues(@RequestParam(value="page",required=false,defaultValue = "1")int page,
                                  @RequestParam(value="count",required=false,defaultValue = "10")int count,
                                     @RequestParam(value="longitude",required = true)double longitude,
                                     @RequestParam(value="latitude",required = true)double latitude){
        
        Pageable<StatusDTO> statusDTO=null;//
        SocialResult<Map<String,Object>> statusVO;
        long userId;
        try {
            userId = this.validateAndGetUser().getUserId();
            logger.info(String.format("get public List with userId=%d",userId));
        }catch (Exception e){
            userId=-1;
            logger.info("getPublicList without user");
        }
        try {
            statusDTO=statusService.listPublicStatuses(page,count);
        } catch (BizException e) {
           if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER){
               logger.error("Error list public List",e);
               return new CommonResult().setMessage("分页参数不正确").setStatus(CommonResult.FAILED_STATUS);
           }
        }
        statusVO=new SocialResult<>();
        statusVO.setCurrentPage(statusDTO.getCurrentPage());
        statusVO.setHasNextPage(statusDTO.getHasNextPage());
        statusVO.setHasPrePage(statusDTO.getHasPrePage());
        if(userId<0){//无token的时候
            for (StatusDTO item : statusDTO.getContent()){
                Map<String,Object>status=StatusDTO2StatusVO(item);//将dto转成vo
                status.put("distance",DataTraslator.GetDistance(latitude, longitude, item.getLongitude(), item.getLatitude()));
                statusVO.getContent().add(status);
            }
        }else{//有token的时候
            for (StatusDTO item : statusDTO.getContent()){
                Map<String,Object>status ;
                status=StatusDTO2StatusVO(item);
                status.put("owned",false);
                status.put("distance",DataTraslator.GetDistance(latitude, longitude, item.getLongitude(), item.getLatitude()));
                if(item.getPublisherId().getUserId()==userId)
                    status.put("owned",true);
                try{
                    Map<String,Object> publisher = (Map<String, Object>) status.get("published");
                    publisher.put("followed",socialService.checkIsFollower(userId,item.getPublisherId().getUserId()));
                    status.put("collected",statusService.isCollected(item.getStatusId(), userId));
                    status.put("favorite",statusService.isLikeStatus(item.getStatusId(), userId));
                }
                catch(BizException e){
                    if((e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER)){
                        logger.error(String.format("Error userId=%d list public list",userId),e);
                        return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("用户id错误");
                    }else {
                        logger.error("Error unknown at listing public list",e);
                        return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("未知错误");
                    }
                }
                statusVO.getContent().add(status);
            }
        }
        statusVO.setCount(statusVO.getContent().size());
        return statusVO;

    }/**
     *得到当前用户的状态
     *需要token
     */
    @RequestMapping(value="/me",method=RequestMethod.POST)
    public Object getMyStatus(@RequestParam(value="page",required=false,defaultValue = "1")int page,
                              @RequestParam(value="count",required=false,defaultValue = "10")int count){
        SocialResult<Map<String,Object>> statusVO = new SocialResult<>();
        UserDetailVO user=validateAndGetUser();
        logger.info(String.format("getMyList userId=%d", user.getUserId()));
        Pageable<StatusDTO> statusDTO =new Pageable<>();
        try {
            statusDTO = statusService.listStatusesByUser(user.getUserId(), page, count);
        } catch (BizException e) {
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER){
                logger.error("Error at listing user Status params error",e);
                return new CommonResult().setMessage("请求参数错误").setStatus(CommonResult.FAILED_STATUS);
            }
        }
        statusVO.setCurrentPage(statusDTO.getCurrentPage());
        statusVO.setHasNextPage(statusDTO.getHasNextPage());
        statusVO.setHasPrePage(statusDTO.getHasPrePage());
        for (StatusDTO item : statusDTO.getContent()){
            Map<String,Object>status=StatusDTO2StatusVO(item);//将dto转成vo
            status.put("longitude",item.getLongitude());
            status.put("latitude",item.getLatitude());
            status.put("owned",true);
            statusVO.getContent().add(status);
        }
        statusVO.setCount(statusVO.getContent().size());
        return statusVO;
    }

    /**
     *
     *  获取当前用户发布的状态，需要token
     *
     *  @param page 页数
     *  @param count 每页数量
     *
     *  @return
     *         {
     *             'currentPage':'',
     *             'maxPage':'',
     *             'count':'',
     *             'allCount':'',
     *             'statuses':[
     *                  {
     *                      'statusId':'',
     *                      'publisher':{
     *                          'userId':''
     *                          'avatarUrl':''
     *                          'nickname':''
     *                      },
     *                      'createAt':'yyyy/MM/dd hh/mm/ss',
     *                      category:{
     *                          'id':'',
     *                          'name':''
     *                      }
     *                      'text':'状态文本内容'
     *                      'images':[
     *                          'url','url'
     *                      ],
     *                      'loveNum':'',
     *                      'commentNum':''
     *                  }
     *             ]
     *         }
     *
     * */
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public Object  listUserStatus(@RequestParam(value="page",required=false,defaultValue = "1")int page,
                               @RequestParam(value="count",required=false,defaultValue = "10")int count,
                                  @RequestParam(value="longitude",required = true)double longitude,
                                  @RequestParam(value="latitude",required = true)double latitude,
                                  @RequestParam(value="userId",required=true)long userId){
        long loginUserId ;
        Pageable<StatusDTO> statusDTO =new Pageable<>();
        try {
            loginUserId = this.validateAndGetUser().getUserId();
        }catch (Exception e){
            logger.info("list user Status but not login");
            loginUserId = -1;
        }

        //根据UserId查询该用户发布的状态
        try {
            statusDTO = statusService.listStatusesByUser(userId, page, count);
        } catch (BizException e) {
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER){
                logger.error("Error at listing user Status params error",e);
                return new CommonResult().setMessage("请求参数错误").setStatus(CommonResult.FAILED_STATUS);
            }
        }
        SocialResult<Map<String,Object>> result= new SocialResult<>();
        result.setCount(statusDTO.getContent().size());
        result.setCurrentPage(statusDTO.getCurrentPage());
        for(StatusDTO item:statusDTO.getContent()){
            Map<String,Object>status=StatusDTO2StatusVO(item);//将dto转成vo
            status.put("distance", DataTraslator.GetDistance(latitude, longitude, item.getLongitude(), item.getLatitude()));
            Map<String,Object> publisher = (Map<String, Object>) status.get("published");
            try{
                if(loginUserId==userId){
                    //当前登陆的用户就是要看的用户
                    status.put("owned",true);
                    publisher.put("followed",false);
                    status.put("collected",false);
                }else{
                    //判断当前登陆用户和发布者关系、状态关系
                    status.put("owned",false);
                    publisher.put("followed",socialService.checkIsFollower(userId,item.getPublisherId().getUserId()));
                    //是否收藏
                    status.put("collected",statusService.isCollected(item.getStatusId(),userId));
                }
                //是否点赞
                status.put("favorite",statusService.isLikeStatus(userId,item.getStatusId()));
            }catch (BizException e){
                logger.error(String.format("Error at checking whether userId=%d is userId=%d 's follower",userId,loginUserId),e);
            }
            result.getContent().add(status);
        }
        result.setCount(result.getContent().size());
        return result;
    }

    /**
     *  根据状态类别获取状态,有token时候返回当前用户和发布者之间关系
     *  以及当前用户和状态关系
     *
     *
     * */
    @RequestMapping(value = "/category",method = RequestMethod.POST)
    public Object listCategoryStatus(
            @RequestParam(value="categoryId",required=false,defaultValue = "1")int categoryId,
            @RequestParam(value="page",required=false,defaultValue = "1")int page,
            @RequestParam(value="count",required=false,defaultValue = "10")int count,
            @RequestParam(value="longitude",required = true)double longitude,
            @RequestParam(value="latitude",required = true)double latitude){
        Pageable<StatusDTO> statusDTO=new Pageable<>();
        SocialResult<Map<String,Object>> statusVO;
        long userId;
        try {
            userId = this.validateAndGetUser().getUserId();
        }catch(Exception e){
            logger.info("get category status no token");
            userId=-1;
        }
        try {
            statusDTO=statusService.listStatusesByCategory(categoryId, page, count);
        } catch (BizException e) {

        }
        statusVO=new SocialResult<>();
        statusVO.setCurrentPage(statusDTO.getCurrentPage());
        statusVO.setHasNextPage(statusDTO.getHasNextPage());
        statusVO.setHasPrePage(statusDTO.getHasPrePage());

        if(userId<0){//无token的时候
            for (StatusDTO item : statusDTO.getContent()){
                Map<String,Object>status=StatusDTO2StatusVO(item);//将dto转成vo
                status.put("distance",DataTraslator.GetDistance(latitude, longitude, item.getLongitude(), item.getLatitude()));
                statusVO.getContent().add(status);
            }
        }else{//有token的时候
            for (StatusDTO item : statusDTO.getContent()){
                Map<String,Object>status ;
                status=StatusDTO2StatusVO(item);
                status.put("owned",false);
                status.put("distance",DataTraslator.GetDistance(latitude, longitude, item.getLongitude(), item.getLatitude()));
                if(item.getPublisherId().getUserId()==userId)
                    status.put("owned",true);

                try{
                    Map<String,Object> publisher = (Map<String, Object>) status.get("published");
                    publisher.put("followed",socialService.checkIsFollower(userId,item.getPublisherId().getUserId()));
                    status.put("collected",statusService.isCollected(item.getStatusId(), userId));
                    status.put("favorite",statusService.isLikeStatus(item.getStatusId(), userId));
                }
                catch(BizException e){
                    if((e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER)){
                        logger.error(String.format("Error userId=%d list public list",userId),e);
                        return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("用户id错误");
                    }else {
                        logger.error("Error unknown at listing public list",e);
                        return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("未知错误");
                    }
                }
                statusVO.getContent().add(status);
            }
        }
        statusVO.setCount(statusVO.getContent().size());
        return statusVO;
    }
    @RequestMapping(value="/location",method=RequestMethod.POST)
    /**
     * 根据locaiton获取状态信息 不需要token
     */
    public Object getStatusByLocation( @RequestParam(value="city",required=true)String city,
                                       @RequestParam(value="district",required =true)String district,
                                       @RequestParam(value="page",required=false,defaultValue = "1")int page,
                                       @RequestParam(value="count",required=false,defaultValue = "10")int count,
                                       @RequestParam(value="longitude",required = true)double longitude,
                                       @RequestParam(value="latitude",required = true)double latitude){
        Pageable<StatusDTO> statusDTO=new Pageable<>();
        SocialResult<Map<String,Object>> statusVO;
        long userId;
        try {
            userId = this.validateAndGetUser().getUserId();
        }catch(Exception e){
            logger.info("get category status no token");
            userId=-1;
        }
        try {
            statusDTO=statusService.listStatusByLocation(city, district, page, count);
        } catch (BizException e) {

        }
        statusVO=new SocialResult<>();
        statusVO.setCurrentPage(statusDTO.getCurrentPage());
        statusVO.setHasNextPage(statusDTO.getHasNextPage());
        statusVO.setHasPrePage(statusDTO.getHasPrePage());

        if(userId<0){//无token的时候
            for (StatusDTO item : statusDTO.getContent()){
                Map<String,Object>status=StatusDTO2StatusVO(item);//将dto转成vo
                status.put("distance",DataTraslator.GetDistance(latitude, longitude, item.getLongitude(), item.getLatitude()));
                statusVO.getContent().add(status);
            }
        }else{//有token的时候
            for (StatusDTO item : statusDTO.getContent()){
                Map<String,Object>status ;
                status=StatusDTO2StatusVO(item);
                status.put("owned",false);
                status.put("distance",DataTraslator.GetDistance(latitude, longitude, item.getLongitude(), item.getLatitude()));
                if(item.getPublisherId().getUserId()==userId)
                    status.put("owned",true);

                try{
                    status.put("followed",socialService.checkIsFollower(userId,item.getPublisherId().getUserId()));
                    status.put("collected",statusService.isCollected(item.getStatusId(), userId));
                    status.put("favorite",statusService.isLikeStatus(item.getStatusId(), userId));
                }
                catch(BizException e){
                    if((e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER)){
                        logger.error(String.format("Error userId=%d list public list",userId),e);
                        return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("用户id错误");
                    }else {
                        logger.error("Error unknown at listing public list",e);
                        return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("未知错误");
                    }
                }
                statusVO.getContent().add(status);
            }
        }
        statusVO.setCount(statusVO.getContent().size());
        return statusVO;
    }

    /**
     *  删除发布状态，需要token
     *
     *  @param statusId 状态Id
     *
     *  @return {'status':'','message':''}
     * */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public Object deleteStatus(@RequestParam(value="statusId",required=true)int statusId){
        UserDetailVO userDetailVO = this.validateAndGetUser();
        try {
            statusService.deleteStatus(statusId);
        } catch (BizException e) {
            logger.error(String.format("Error at deleting status  userId=%d ",userDetailVO.getUserId()),e);
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER){
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("请求参数错误");
            }else if(e.getErrorCode()==BizException.ERROR_CODE_STATUS_NOT_FOUND){
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("无此状态，请重试");
            }
        }
        return new CommonResult()
                .setStatus(CommonResult.SUCCESS_STATUS)
                .setMessage("删除成功");
    }

    /**
     * 发布状态，需要token
     *
     * @return {'status':'','message':''}
     * */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addStatus(@RequestBody String payload){
        logger.info(payload);

        UserDetailVO userDetailVO = this.validateAndGetUser();
        /**
         {
         "categoryId":" 类别ID ",
         "text":" 文本内容 ",
         "longtiude":"",
         "latitude": "",
         "city":"",
         "district":"",
         "images":[
             {
             "url": "",
             "quality":"high / normal",
             }
         ]
         }
         * */
        try{
            JSONObject jsonObj=new JSONObject(payload);

            List<StatusImageDTO> images=new LinkedList<>();

            if(!jsonObj.isNull("images")){
                JSONArray arr=jsonObj.getJSONArray("images");

                for(int index=0;index!=arr.length();index++){
                    JSONObject imgJsonObj=arr.getJSONObject(index);

                    StatusImageDTO imgDto=new StatusImageDTO();
                    imgDto.setUrl(imgJsonObj.getString("url"));
                    imgDto.setQuality(imgJsonObj.getString("quality"));

                    images.add(imgDto);
                }
            }

          statusService.addStatus(userDetailVO.getUserId(),
                  jsonObj.getLong("categoryId"),
                  jsonObj.getString("text"),
                  jsonObj.getDouble("longitude"),
                  jsonObj.getDouble("latitude"),
                  jsonObj.getString("city") + "," + jsonObj.getString("district"),
                  images
          );

            return new CommonResult()
                    .setStatus(CommonResult.SUCCESS_STATUS)
                    .setMessage("发布成功");
        }catch (JSONException e){

            return new CommonResult()
                    .setStatus(CommonResult.FAILED_STATUS)
                    .setMessage("缺少字段");
        }catch (Exception e){
            return new CommonResult()
                    .setStatus(CommonResult.FAILED_STATUS)
                    .setMessage(e.getMessage());
        }

    }

    /**
     * 状态点赞,需要token
     *
     * @param statusId 需要点赞的状态ID
     *
     * @return {'status':'','message':''}
     * */
    @RequestMapping(value = "/like/add",method = RequestMethod.POST)
    public Object likeStatus(@RequestParam(value="statusId",required=true)int statusId){
        JSONObject jsonObject;
        UserDetailVO userDetailVO;
        long toUserId;
        try {
            userDetailVO = this.validateAndGetUser();
        }catch(Exception e){
            logger.error("list user Status Error not login",e);
            return new CommonResult().setMessage("请登录后再获取列表").setStatus(CommonResult.FAILED_STATUS);
        }
        logger.info(String.format("userId=%d like statusId=%d", userDetailVO.getUserId(), statusId));
        try {
            statusService.likeStatus(userDetailVO.getUserId(), statusId);
        } catch (BizException e) {
            logger.error(String.format("Error at liking status  userId=%d statusId=%d",userDetailVO.getUserId(),statusId),e);
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER){
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("请求参数错误");
            }else if(e.getErrorCode()==BizException.ERROR_CODE_INSTANCE_NOT_FOUND){
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("账户或状态实例未找到！，请重试");
            }
        }

        jsonObject = new JSONObject();
        jsonObject.put("statusIs",statusId);
        jsonObject.put("formAccount", new AccountSimpleVO(userDetailVO.getUserId(), userDetailVO.getNickname(), userDetailVO.getAvatarUrl()));
        jsonObject.put("actionCode", NotificationEndpoint.SEND_CODE_FAVORITE);
        try {
            toUserId=statusService.getStatusById(statusId).getPublisherId().getUserId();
            NotificationEndpoint.sendMessage(toUserId, jsonObject.toString());//发送推送消息
        } catch (BizException e) {
            logger.error(String.format("Error at pushing messing to status's owner statusId = %d",statusId),e);
        }

        return new CommonResult()
                .setStatus(CommonResult.SUCCESS_STATUS)
                .setMessage("点赞成功");
    }

    /**
     * 取消点赞,需要token
     *
     * @param statusId 需要取消点赞的状态ID
     *
     * @return {'status':'','message':''}
     * 推送消息的格式
     * {
     *     'statusId':'
     *    'formAccount': {
     *          'userId':''
     *          'avatarUrl':''
     *          'nickname':''
     *     }
     *     'tag':
     * }
     * */
    @RequestMapping(value = "/like/delete",method = RequestMethod.POST)
    public Object  unlikeStatus(@RequestParam(value="statusId",required=true)int statusId){
        UserDetailVO userDetailVO = this.validateAndGetUser();
        logger.info(String.format("username=%s unlike statusId=%s", userDetailVO.getNickname(), statusId));
        try {
            statusService.unlikeStatus(userDetailVO.getUserId(), statusId);
        } catch (BizException e) {
            logger.error(String.format("Error at unlike status userId=%d statusId=%d",userDetailVO.getUserId(),statusId),e);
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER)
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("请求参数错误");
            else if(e.getErrorCode()==BizException.ERROR_CODE_STATUS_NOT_FOUND)
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("无此状态，请检查后再试一次");
        }
        return new CommonResult()
                .setStatus(CommonResult.SUCCESS_STATUS)
                .setMessage("取消点赞成功");
    }

    /**
     * 获取用户点赞的状态
     * @param page
     * @param count
     * @param longitude
     * @param latitude
     * @param userId
     * @return
     */
    @RequestMapping(value="/like/list",method=RequestMethod.POST)
    public Object listLikedStatus( @RequestParam(value="page",required=false,defaultValue = "1")int page,
                                   @RequestParam(value="count",required=false,defaultValue = "10")int count,
                                   @RequestParam(value="longitude",required = true)double longitude,
                                   @RequestParam(value="latitude",required = true)double latitude,
                                   @RequestParam(value="userId",required = true)long userId){
        long loginUserId ;
        Pageable<StatusDTO> statusDTO =new Pageable<>();
        try {
            loginUserId = this.validateAndGetUser().getUserId();
        }catch (Exception e){
            logger.info("list user Status but not login");
            loginUserId = -1;
        }
        //根据UserId查询该用户发布的状态
        try {
            statusDTO = statusService.listLikedStatusByUserId(userId, page, count);
        } catch (BizException e) {
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER){
                logger.error("Error at listing user liked Status ",e);
                return new CommonResult().setMessage("请求参数错误").setStatus(CommonResult.FAILED_STATUS);
            }
        }
        SocialResult<Map<String,Object>> result= new SocialResult<>();
        result.setCount(statusDTO.getContent().size());
        result.setCurrentPage(statusDTO.getCurrentPage());

        for(StatusDTO item:statusDTO.getContent()){
            Map<String,Object>status=StatusDTO2StatusVO(item);//将dto转成vo
            try{
                if(loginUserId==userId){
                    //当前登陆的用户就是要看的用户
                    status.put("owned",true);
                    status.put("followed",false);
                    status.put("collected",false);
                }else{
                    //判断当前登陆用户和发布者关系、状态关系
                    status.put("owned",false);
                    //是否关注当前用户
                    Map<String,Object> publisher = (Map<String, Object>) status.get("published");
                    publisher.put("followed",item.getPublisherId().getUserId());
                    //是否收藏
                    status.put("collected",statusService.isCollected(item.getStatusId(),userId));
                }
                //是否点赞
                status.put("favorite",statusService.isLikeStatus(userId,item.getStatusId()));
            }catch (BizException e){
                logger.error(String.format("Error at checking whether userId=%d is userId=%d 's follower",userId,loginUserId),e);
            }
            result.getContent().add(status);
        }
        result.setCount(result.getContent().size());
        return result;
    }
    /**
     * 添加留言,需要Token
     *
     * @param statusId 需要留言的状态ID
     * @param content 留言内容
     *
     * @return {'status':'','message':''}
     * 推送消息的格式
     * {
     *     'statusId':'
     *    'formAccount': {
     *          'userId':''
     *          'avatarUrl':''
     *          'nickname':''
     *     }
     *     'msg':
     *     'tag'
     * }
     * */
    @RequestMapping(value = "/comment/add",method = RequestMethod.POST)
    public Object  addComment(
            @RequestParam(value="statusId",required=true)int statusId,
            @RequestParam(value="content",required = true)String content){
        JSONObject jsonObject;
        UserDetailVO userDetailVO;
        long toUserId;
        userDetailVO = this.validateAndGetUser();
        logger.info(String.format("userId=%d comment=%s", userDetailVO.getUserId(), content));
        try {
            statusService.addComment(userDetailVO.getUserId(), statusId, content);
        } catch (BizException e) {
            logger.error(String.format("Error at adding new comment statusId=%d,userId=%d",statusId,userDetailVO.getUserId()),e);
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER)
                return new CommonResult().setMessage("参数错误咯").setStatus(CommonResult.FAILED_STATUS);
        }

        jsonObject = new JSONObject();
        jsonObject.put("statusIs",statusId);
        jsonObject.put("formAccount",String.format("{userid:%s,nickname:%s}", String.valueOf(userDetailVO.getUserId()), userDetailVO.getNickname()));
        jsonObject.put("msg", content);
        jsonObject.put("tag", NotificationEndpoint.SEND_CODE_COMMENT);
        try {
            toUserId=statusService.getStatusById(statusId).getPublisherId().getUserId();
            NotificationEndpoint.sendMessage(toUserId, jsonObject.toString());//发送推送消息
        } catch (BizException e) {
            logger.error(String.format("Error at pushing messing to status's owner statusId = %d",statusId),e);
        }
        return new CommonResult()
                .setStatus(CommonResult.SUCCESS_STATUS)
                .setMessage("回复成功");
    }

    /**
     * 获取状态下的所有留言
     *
     * @param statusId 需要留言的状态ID
     *
     *
     * */
    @RequestMapping(value = "/comment/list",method = RequestMethod.POST)
    public Object  listCommentByStatusId(
            @RequestParam(value="statusId",required=true)int statusId){
        logger.info(String.format("get statusId=%d comment ", statusId));
        List<CommentDTO> commentDTOs = new LinkedList<>();//
        List<Map<String,Object>>comments = new ArrayList<>();
        try {
            commentDTOs = statusService.listCommentByStatus(statusId);
        } catch (BizException e) {
            logger.error(String.format("Error at getting statusId=%d",statusId),e);
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER)
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setStatus("请求参数错误");
        }
        for(CommentDTO commentDTO:commentDTOs){
            Map<String,Object> comment = new HashMap<>();
            comment.put("commentId",commentDTO.getCommentId());
            comment.put("publisher",new AccountSimpleVO(commentDTO.getPublisher().getUserId(), commentDTO.getPublisher().getNickname(), commentDTO.getPublisher().getAvatarUrl()));
            comment.put("content",commentDTO.getContent());
            comment.put("createAt",DataTraslator.LongToTimePast(commentDTO.getCreateAt().getTime()));
            comments.add(comment);
        }
        return comments;
    }

    /**
     * 添加收藏  需要有token
     * @param statusId 这条状态的id
     * @return
     *  推送消息的格式
     * {
     *     'statusId':'
     *    'formAccount': {
     *          'userId':''
     *          'avatarUrl':''
     *          'nickname':''
     *     }
     *     'tag'
     * }
     */
    @RequestMapping(value="/collect/add",method=RequestMethod.POST)
    public Object  addCollection(@RequestParam(value = "statusId", required = true) int statusId) {
        JSONObject jsonObject;
        UserDetailVO userDetailVO = this.validateAndGetUser();
        long toUserId;
        long userId=userDetailVO.getUserId();
        logger.info(String.format("userId=%d add statusId=%d as collection", userId, statusId));
        try {
            statusService.addCollection(statusId, userId);
        } catch (BizException e) {
            logger.error(String.format("Error at getting statusId=%d", statusId), e);
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER)
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setStatus("请求参数错误");
        }

        jsonObject = new JSONObject();
        jsonObject.put("statusIs",statusId);
        jsonObject.put("formAccount", new AccountSimpleVO(userDetailVO.getUserId(), userDetailVO.getNickname(), userDetailVO.getAvatarUrl()));
        jsonObject.put("tag", NotificationEndpoint.SEND_CODE_COLLECT);
        try {
            toUserId=statusService.getStatusById(statusId).getPublisherId().getUserId();
            NotificationEndpoint.sendMessage(toUserId,jsonObject.toString());//发送推送消息
        } catch (BizException e) {
            logger.error(String.format("Error at pushing messing to status's owner statusId = %d", statusId),e);
        }
        return new CommonResult()
                .setStatus(CommonResult.SUCCESS_STATUS)
                .setMessage("收藏成功");
    }

    /**
     * 取消收藏，需要有token
     * @return
     */
    @RequestMapping(value="/collect/delete",method=RequestMethod.POST)
    public Object deleteCollection(@RequestParam(value="statusId",required=true)int statusId){
        long userId;
        try {
            userId = this.validateAndGetUser().getUserId();
        }catch(Exception e){
            logger.error("list user Status Error not login", e);
            return new CommonResult().setMessage("请登录后再获取列表").setStatus(CommonResult.FAILED_STATUS);
        }
        logger.info(String.format("userId=%d delete collection statusId=%d ", userId, statusId));
        try {
            statusService.removeCollection(statusId, userId);
        } catch (BizException e) {
            logger.error(String.format("Error at deleting status userId=%d",userId),e);
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER)
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("请求参数错误");
            else if(e.getErrorCode()==BizException.ERROR_CODE_STATUS_NOT_FOUND)
                return new CommonResult().setStatus(CommonResult.FAILED_STATUS).setMessage("无此状态，请检查后再试一次");
        }
        return new CommonResult()
                .setStatus(CommonResult.SUCCESS_STATUS)
                .setMessage("取消收藏");
    }

    /**
     * 得到用户收藏的状态
     * @return
     */
    @RequestMapping(value="/collect/list",method=RequestMethod.POST)
    public Object getCollectedStatus( @RequestParam(value="page",required=false,defaultValue = "1")int page,
                                      @RequestParam(value="count",required=false,defaultValue = "10")int count,
                                      @RequestParam(value="longitude",required = true)double longitude,
                                      @RequestParam(value="latitude",required = true)double latitude,
                                      @RequestParam(value="userId",required = true)long userId){
        long loginUserId ;
        Pageable<StatusDTO> statusDTO =new Pageable<>();
        try {
            loginUserId = this.validateAndGetUser().getUserId();
        }catch (Exception e){
            logger.info("list user Status but not login");
            loginUserId = -1;
        }
        //根据UserId查询该用户发布的状态
        try {
            statusDTO = statusService.listLikedStatusByUserId(userId, page, count);
        } catch (BizException e) {
            if(e.getErrorCode()==BizException.ERROR_CODE_NULL_POINTER){
                logger.error("Error at listing user liked Status ",e);
                return new CommonResult().setMessage("请求参数错误").setStatus(CommonResult.FAILED_STATUS);
            }
        }
        SocialResult<Map<String,Object>> result= new SocialResult<>();
        result.setCount(statusDTO.getContent().size());
        result.setCurrentPage(statusDTO.getCurrentPage());

        for(StatusDTO item:statusDTO.getContent()){
            Map<String,Object>status=StatusDTO2StatusVO(item);//将dto转成vo
            try{
                if(loginUserId==userId){
                    //当前登陆的用户就是要看的用户
                    status.put("owned",true);
                    status.put("followed",false);
                    status.put("collected",false);
                }else{
                    //判断当前登陆用户和发布者关系、状态关系
                    status.put("owned",false);
                    //是否关注当前用户
                    Map<String,Object> publisher = (Map<String, Object>) status.get("published");
                    publisher.put("followed",item.getPublisherId().getUserId());
                    //是否收藏
                    status.put("collected",statusService.isCollected(item.getStatusId(),userId));
                }
                //是否点赞
                status.put("favorite",statusService.isLikeStatus(userId,item.getStatusId()));
            }catch (BizException e){
                logger.error(String.format("Error at checking whether userId=%d is userId=%d 's follower",userId,loginUserId),e);
            }
            result.getContent().add(status);
        }
        result.setCount(result.getContent().size());
        return result;
    }
    private Map<String,Object> StatusDTO2StatusVO(StatusDTO statusDTO){
        Map<String,Object> status= new HashMap<>();
        Map<String,Object> publisher= new HashMap<>();
        List<ImageVO> urls=new ArrayList<>();
        status.put("statusId", statusDTO.getStatusId());
        publisher.put("account_id",statusDTO.getPublisherId().getUserId());
        publisher.put("nickname",statusDTO.getPublisherId().getNickname());
        publisher.put("avatar_url",statusDTO.getPublisherId().getAvatarUrl());
        status.put("published",publisher);
        status.put("category", new StatusCategoryVO(statusDTO.getCategory().getCategoryId(), statusDTO.getCategory().getName()));
        status.put("text", statusDTO.getText());
        status.put("create_at", DataTraslator.LongToTimePast(statusDTO.getCreateAt().getTime()));
        status.put("city",statusDTO.getCity());
        status.put("district",statusDTO.getDistrict());
        status.put("comment_num", statusDTO.getCommentNum());
        status.put("like_num", statusDTO.getLoveNum());
        for(StatusImageDTO statusImageDTO:statusDTO.getImages()) 
            urls.add(new ImageVO().setOrigin_url(statusImageDTO.getUrl()).setQuality(statusImageDTO.getQuality()));
        status.put("images",urls);

        return status;
    }

        
    /**
     * 获取状态类别的列表
     *
     * */
    @RequestMapping(value="/category/list",method=RequestMethod.POST)
    public Object listCategory(){
        List<Map<String,Object>> result=new LinkedList<>();
        List<StatusCategoryDTO>  categoryDTOs= null ;
        try {
            categoryDTOs = statusService.listAllCategory();
        } catch (BizException e) {
            logger.error("Error");//这里还没有定义exception
        }
        for(StatusCategoryDTO categoryDTO:categoryDTOs){
           Map<String,Object> categoryVO=new HashMap<>();
           categoryVO.put("category_id",categoryDTO.getCategoryId());
           categoryVO.put("category_name",categoryDTO.getName());
           categoryVO.put("category_img",categoryDTO.getImageUrl());
           categoryVO.put("category_num",categoryDTO.getStatusNum());

           result.add(categoryVO);
       }

        return result;
    }
    /**
     *
     * */
}
