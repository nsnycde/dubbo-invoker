package com.nsn.dubbo.dubboinvoker.web.vo.response;

import lombok.Data;

@Data
public class UserRsp {

    private Integer userId;
    private String userName;
    private String password;
    private String phone;

}
