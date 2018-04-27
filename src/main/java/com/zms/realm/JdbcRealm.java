package com.zms.realm;

import com.zms.service.UserService;
import com.zms.vo.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author:zms
 * @Description:Jdbc实现域 负责从中校验用户
 * 在没有集成CAS之前 用户验证交给UserRealm(继承AuthorizingRealm 重写用户验证和
 * 用户授权方法) 集成CAS后 用户验证依然交给UserRealm(继承CASRealm 而CASRealm
 * 又继承AuthorizingRealm)
 * ps:AuthorizingRealm继承了AuthencationRealm
 * 即用户授权和验证是同一体系
 * Authencation 用户身份凭证信息
 * Authorization 用户权限信息
 * Realm 数据域 可以是jdbc也可以是其他例如配置文件
 * @Date:Create On 2018/4/26 18:09
 */
public class JdbcRealm  extends AuthorizingRealm {
    /**
     * @Author:zms
     *
     * @Description:subject.login()方法调用时 调用此方法判断用户的校验信息 组装成一个
     * 加密且封装的SimpleAuthenticationInfo返回
     *
     * @Date:2018/4/26 18:10
     *
     */

    @Autowired
    private UserService userService;
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String)token.getPrincipal();
        System.out.printf("进来了");
        User user = userService.queryUserByName(username);
        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        if(Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); //帐号锁定
        }

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getUsername()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }
    /**
     * @Author:zms
     *
     * @Description:获取用户的权限信息
     *
     * @Date:2018/4/26 18:12
     *
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


}
