package com.zms.vo;

import java.util.Date;

/**
 * @Author:zms
 * @Description:权限
 * @Date:Create On 2018/4/26 15:11
 */
public class Permission {

    private int id;//权限id

    private String permission;//权限

    private String description;//权限描述

    private Date createtime;//创建时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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
