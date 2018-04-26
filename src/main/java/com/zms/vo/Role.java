package com.zms.vo;

import java.util.Date;

/**
 * @Author:zms
 * @Description:角色
 * @Date:Create On 2018/4/26 15:11
 */
public class Role {

    private int id;//角色id

    private String role;//用户角色

    private String description;//用户角色描述

    private Date createtime;//创建时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
