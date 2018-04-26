package com.zms.service;

import com.zms.vo.User;

/**
 * @Author:zms
 * @Description:
 * @Date:Create On 2018/4/26 15:27
 */
public interface UserService {

    public boolean addUser(User user);

    public boolean login(String userName,String password);
}
