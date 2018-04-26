package com.zms.service.impl;

import com.zms.mapper.UserMapper;
import com.zms.service.UserService;
import com.zms.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:zms
 * @Description:UserService
 * @Date:Create On 2018/4/26 15:27
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public boolean addUser(User user) {
        return userMapper.addUser(user)==0?Boolean.FALSE:Boolean.TRUE;
    }

    @Override
    public boolean login(String userName,String password) {
        System.out.println(userName);
        System.out.println(password);
        return userMapper.login(userName,password)==null?Boolean.FALSE:Boolean.TRUE;
    }
}
