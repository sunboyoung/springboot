package com.zms.vo;

import java.util.Date;

/**
 * @Author:zms
 * @Description:用户
 * @Date:Create On 2018/4/26 15:11
 */
public class User {

    private int id;//用户id

    private String username;//用户名

    private String password;//用户密码

    private String salt;//盐值

    private Date createtime;//创建时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
