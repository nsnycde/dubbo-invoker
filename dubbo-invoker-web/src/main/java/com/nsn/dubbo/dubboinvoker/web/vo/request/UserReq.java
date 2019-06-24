package com.nsn.dubbo.dubboinvoker.web.vo.request;

import lombok.Data;

@Data
public class UserReq {

    private Integer userId;
    private String userName;
    private String password;
    private String phone;

}
