package com.nsn.dubbo.dubboinvoker.service.impl;

import com.nsn.dubbo.dubboinvoker.dal.dao.UserDao;
import com.nsn.dubbo.dubboinvoker.dal.po.User;
import com.nsn.dubbo.dubboinvoker.dal.po.UserExample;
import com.nsn.dubbo.dubboinvoker.service.UserService;
import com.nsn.dubbo.dubboinvoker.service.enumeration.ReturnEnum;
import com.nsn.dubbo.dubboinvoker.service.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author nsn
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User queryUser(String name, String pwd) {
        Assert.hasText(name, "name不能为空!");
        Assert.hasText(pwd, "pwd不能为空!");
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(name).andPwdEqualTo(pwd);
        List<User> users = userDao.selectByExample(example);
        if(users.size() > 1){
            throw new BizException(ReturnEnum.EXCEPTION_USER_NAME_EXIST);
        }
        return users.size() == 1 ? users.get(0) : null;
    }

    @Override
    public int addUser(String name, String pwd) {
        Assert.hasText(name, "name不能为空!");
        Assert.hasText(pwd, "pwd不能为空!");
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(name);
        if(userDao.selectByExample(example).size() > 0){
            throw new BizException(ReturnEnum.EXCEPTION_USER_NAME_EXIST);
        }
        User user = new User();
        user.setName(name);
        user.setPwd(pwd);
        //普通用户
        user.setType(1);
        return this.insertSelective(user);
    }

    @Override
    public int delUser(Long userId) {
        Assert.notNull(userId, "userId不能为空!");
        User user = new User();
        user.setUserId(userId);
        //删除
        user.setDelFlag(1);
        return this.updateByPrimaryKey(user);
    }

    /**
     * 包装dao
     * @param user
     * @return
     */
    private int updateByPrimaryKey(User user){
        user.setUpdateTime(new Date());
        return userDao.updateByPrimaryKey(user);
    }

    /**
     * 包装dao
     * @param user
     * @return
     */
    private int insertSelective(User user){
        user.setCreateTime(new Date());
        return userDao.insertSelective(user);
    }
}
