package com.nsn.dubbo.dubboinvoker.service;

import com.nsn.dubbo.dubboinvoker.dal.po.User;

/**
 * @author nsn
 */
public interface UserService {

    /**
     * 查询用户
     * @param name
     * @param pwd
     * @return
     */
    User queryUser(String name, String pwd);

    /**
     * 添加用户
     * @param name
     * @param pwd
     * @return
     */
    int addUser(String name, String pwd);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    int delUser(Long userId);
}
