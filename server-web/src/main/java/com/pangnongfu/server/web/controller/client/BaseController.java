package com.pangnongfu.server.web.controller.client;

import com.pangnongfu.server.web.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author:zhangyu
 * create on 15/9/20.
 */
public class BaseController {

    private static Map<String,Object> kvCache=new ConcurrentHashMap<>();

    @Autowired
    private HttpServletRequest httpServletRequest;

    protected UserDetailVO validateAndGetUser(){
        if(httpServletRequest!=null){
            String token=httpServletRequest.getParameter("token");

            if(!StringUtils.isEmpty(token)){
                UserDetailVO userDetailVO = (UserDetailVO) kvCache.get(token);

                if(userDetailVO !=null && userDetailVO.getUserId()>0){
                    return userDetailVO;
                }else{
                    throw new RuntimeException("validate failed");
                }
            }else{
                throw new RuntimeException("token not found");
            }
        }else{
            throw new RuntimeException("HttpServletRequest init failed");
        }
    }

    protected void cache(String key,Object value){
        kvCache.put(key,value);
    }

    protected void removeCache(String key){
        kvCache.remove(key);
    }
}