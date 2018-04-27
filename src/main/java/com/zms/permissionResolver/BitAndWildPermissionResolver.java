package com.zms.permissionResolver;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * @Author:zms
 *
 * @Description:自定义权限匹配
 *
 * @Date:2018/4/27 15:05
 *
 */
public class BitAndWildPermissionResolver implements PermissionResolver {


    public Permission resolvePermission(String permissionString) {
        if(permissionString.startsWith("+")) {
            return new BitPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}