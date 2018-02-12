package com.pangnongfu.server.web.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Author:zhangyu
 * create on 15/9/19.
 */
@Controller
@RequestMapping("/debug")
public class DebugController {

    @RequestMapping("/webapi")
    public String webapi(){

        return "webapi";
    }

}
