package com.pangnongfu.server.web.controller.client;

import com.pangnongfu.server.bal.api.AccountService;
import com.pangnongfu.server.bal.api.CategoryService;
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
@RequestMapping("/category")
public class CategoryController extends BaseController{

    private static Logger logger= LoggerFactory.getLogger(CategoryController.class);

    private CategoryService categoryService;
    @Autowired
    public void setCategoryService(CategoryService categoryService){this.categoryService=categoryService;}
    @RequestMapping(value="/simple/list",method = RequestMethod.POST)
    public Object listSimpleCategory(){
        return null;
    }
}
