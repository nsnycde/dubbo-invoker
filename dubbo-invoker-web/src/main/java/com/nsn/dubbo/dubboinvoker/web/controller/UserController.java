package com.nsn.dubbo.dubboinvoker.web.controller;

import com.nsn.dubbo.dubboinvoker.service.UserService;
import com.nsn.dubbo.dubboinvoker.service.enumeration.ReturnEnum;
import com.nsn.dubbo.dubboinvoker.service.exception.BizException;
import com.nsn.dubbo.dubboinvoker.web.vo.BasicResponse;
import com.nsn.dubbo.dubboinvoker.web.vo.response.UserRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author donghao
 */
@Slf4j
@RequestMapping("user")
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "get",method = RequestMethod.POST)
    @ResponseBody
    public BasicResponse<UserRsp> getUserById(Integer id){
        log.info("查询用户id:{}", id);
        BasicResponse<UserRsp> rsp = new BasicResponse<>();
        try {


        } catch (IllegalArgumentException e){
            rsp.setCodeAndMsg(ReturnEnum.FAIL.getCode(), e.getMessage());
        } catch (BizException e){
            rsp.setCodeAndMsg(e);
        } catch (Exception e) {
            log.error("查询用户异常,consumerId:{}", id, e);
            rsp.setCodeAndMsg(ReturnEnum.FAIL);
        }
        log.info("查询用户出参:{},consumerId:{}", id);
        return rsp;
    }

}
