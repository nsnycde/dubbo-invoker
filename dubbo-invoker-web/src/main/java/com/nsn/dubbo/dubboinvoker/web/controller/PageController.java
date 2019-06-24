package com.nsn.dubbo.dubboinvoker.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 描述: 页面控制controller
 *
 * @author nsn
 */

@Controller
@RequestMapping("page")
public class PageController {

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("detail")
    public String onlineDetail(){
        return "onlineDetail";
    }

    @RequestMapping("configManager")
    public String onConfigManager(){
        return "configManager";
    }
}
