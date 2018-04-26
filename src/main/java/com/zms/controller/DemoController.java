package com.zms.controller;

import com.zms.service.UserService;
import com.zms.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:zms
 * @Description:Spring boot Controller测试
 * @Date:Create On 2018/4/26 14:33
 */
@RestController
@RequestMapping("user")
public class DemoController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "login",method = {RequestMethod.GET})
    public String  hello(String userName,String password){
        User user=new User();
        user.setUsername(userName==null?"zms":userName);
        user.setPassword(password==null?"123":password);
        boolean isLogin=userService.login(userName,password);
        boolean isAdd=userService.addUser(user);
        System.out.println(isLogin);
        return "鸟";
    }
}
