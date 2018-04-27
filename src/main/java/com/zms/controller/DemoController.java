package com.zms.controller;

import com.zms.service.UserService;
import com.zms.to.ResponseBean;
import com.zms.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

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

    private Logger logger = Logger.getLogger(DemoController.class.toString());


    @RequestMapping(value = "login",method = {RequestMethod.GET})
    public @ResponseBody  ResponseBean login(String userName, String password){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName,password);
        token.setRememberMe(Boolean.FALSE);
        System.out.println(token.getPassword());
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            logger.info(e.toString());
            return new ResponseBean(ResponseBean.FAIL, "用户名/密码错误");
        } catch (IncorrectCredentialsException e) {
            logger.info(e.toString());
            return new ResponseBean(ResponseBean.FAIL, "用户名/密码错误");
        } catch (ExcessiveAttemptsException e) {
            // TODO: handle exception
            logger.info(e.toString());
            return new ResponseBean(ResponseBean.FAIL, "您的账户已被锁定十分钟,请稍后再试");
        } catch (AuthenticationException e) {
            // 其他错误，比如锁定，如果想单独处理请单独catch处理
            logger.info(e.toString());
        }

        return new ResponseBean(ResponseBean.SUCCESS, "登录成功");
    }

    @RequestMapping(value = "regisUser",method = {RequestMethod.GET})
    public String regisUser(String userName,String password){
        User user=new User();
        user.setUsername(userName==null?"zms":userName);
        user.setPassword(password==null?"123":password);
        return userService.addUser(user)==Boolean.TRUE?"注册成功":"注册失败";
    }
}
