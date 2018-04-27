package com.zms.realm;

import com.zms.service.UserService;
import com.zms.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
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
public class UserRealm extends CasRealm {


    @Autowired
    private UserService userService;

    /**
     * @Author:zms
     *
     * @Description:用户信息校验
     *
     * @Date:2018/4/27 11:16
     *
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("dsadsada");

        String userName=(String)token.getPrincipal();
        User user=userService.queryUserByName(userName);
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
     * @Description:用户授权
     *
     * @Date:2018/4/27 11:16
     *
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("进来了");
        return super.doGetAuthorizationInfo(principals);
    }

    /**
     * @Author:zms
     *
     * @Description:用户授权时 调用此方法清空以前的权限缓存
     *
     * @Date:2018/4/27 11:11
     *
     */
    @Override
    protected void doClearCache(PrincipalCollection principals) {
        super.doClearCache(principals);
    }
}
