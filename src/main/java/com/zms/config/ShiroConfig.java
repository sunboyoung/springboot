package com.zms.config;

import com.zms.permissionResolver.BitAndWildPermissionResolver;
import com.zms.permissionResolver.MyRolePermissionResolver;
import com.zms.realm.JdbcRealm;
import com.zms.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.ResourceUtils;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author:zms
 * @Description: shiro核心管理器SecurityMannager
 * @Date:Create On 2018/4/26 17:53
 *
 * @Configuration代表把当前类作为是配置类注入
 *
 * @Bean相当于xml里面<bean id="xxxx" class="xxxx"></bean>注入bean
 */

@Configuration
public class ShiroConfig {

    //*****************这部分可以抽出来放配置文件里***************//
    // cas server地址 当前服务器地址
    public static final String casServerUrlPrefix = "http://127.0.0.1";
    // Cas登录页面地址 跳转到cas登录页面
    public static final String casLoginUrl = casServerUrlPrefix + "/login";
    // Cas登出页面地址
    public static final String casLogoutUrl = casServerUrlPrefix + "/logout";
    // 当前工程对外提供的服务地址
    public static final String shiroServerUrlPrefix = "http://127.0.1.28:8080";
    // casFilter UrlPattern
    public static final String casFilterUrlPattern = "/index";
    // 登录地址 用户登录校验时跳转的地址
    public static final String loginUrl = casLoginUrl + "?service=" + shiroServerUrlPrefix + casFilterUrlPattern;
    // 登出地址（casserver启用service跳转功能，需在webapps\cas\WEB-INF\cas.properties文件中启用cas.logout.followServiceRedirects=true）
    public static final String logoutUrl = casLogoutUrl+"?service="+loginUrl;
    // 登录成功地址
//    public static final String loginSuccessUrl = "/index";
    // 权限认证失败跳转地址
    public static final String unauthorizedUrl = "/error/403.html";

    //*********************以上部分可以抽出来放配置文件里********************//

    /**
     * @Author:zms
     * @Description: 只集成shiro采用自定义实现Realm 交给SecurityManager管理
     * @Date:2018/4/26 18:02
     */
    @Bean
    public JdbcRealm getJdbcRealm() {
      JdbcRealm JdbcRealm = new JdbcRealm();
       JdbcRealm.setCredentialsMatcher(getCredentialsMatcher());
        return JdbcRealm;
     }
    /**
     * @Author:zms
     *
     * @Description: 集成CAS使用的Realm 将用户的登录验证以及授权交给CasRealm实现
     *
     * @Date:2018/4/27 11:42
     *
     */
//    @Bean
//    public UserRealm getUserRealm(){
//        UserRealm userRealm=new UserRealm();
//        //cas登录地址前缀
//        userRealm.setCasServerUrlPrefix(casServerUrlPrefix);
//        //用户回掉地址，登录成功后的跳转地址
//        userRealm.setCasService(shiroServerUrlPrefix+casFilterUrlPattern);
//        //默认角色user
//        userRealm.setDefaultRoles("user");
//        //设置密码加密解密方式
//        userRealm.setCredentialsMatcher(getCredentialsMatcher());
//        userRealm.setCachingEnabled(true);
//        userRealm.setAuthenticationCachingEnabled(true);
//        userRealm.setAuthenticationCacheName("authenticationCache");
//        userRealm.setAuthorizationCachingEnabled(true);
//        userRealm.setAuthorizationCacheName("authorizationCache");
//        return userRealm;
//    }
    /**
     * @Author:zms
     *
     * @Description:单点登出的listen
     *
     * @Date:2018/4/27 11:58
     *
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)// 优先级需要高于Cas的Filter
    public ServletListenerRegistrationBean<?> singleSignOutHttpSessionListener(){
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean();
        bean.setListener(new SingleSignOutHttpSessionListener());
        bean.setEnabled(true);
        return bean;
    }
    /**
     * @Author:zms
     *
     * @Description:单点登出的filter
     *
     * @Date:2018/4/27 11:59
     *
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setName("singleSignOutFilter");
        bean.setFilter(new SingleSignOutFilter());
        bean.addUrlPatterns("/*");
        bean.setEnabled(true);
        return bean;
    }
    /**
     * @Author:zms
     * @Description:这个类对密码进行编码
     * @Date:2018/4/26 18:44
     */
    @Bean(name="credentialsMatcher")
    public HashedCredentialsMatcher getCredentialsMatcher() {

        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");//采用md5加密
        credentialsMatcher.setHashIterations(2);//hash迭代次数
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    /**
     * @Author:zms
     *
     * @Description:配置ehcache缓存管理器
     *
     * @Date:2018/4/26 19:02
     *
     */
    public EhCacheManager getEhCacheManager(){
        EhCacheManager ehCacheManager=new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:shiro-ehcache.xml");
        return ehCacheManager;
    }
    /**
     * @Author:zms
     * @Description:注入shiro核心管理器securityManager
     * @Date:2018/4/26 18:01
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager  getDefaultSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置数据域
        securityManager.setRealm(getJdbcRealm());
        //角色权限认证策略
        securityManager.setAuthorizer(getModular());
        // 采用ehcache缓存
        securityManager.setCacheManager(getEhCacheManager());
        // 如果使用cas的remember me 功能需要用到CasSubjectFactory,并交给SecurityManager管理
        securityManager.setSubjectFactory(new CasSubjectFactory());
        return securityManager;
    }
    /**
     * @Author:zms
     *
     * @Description:Spring提供的拦截器注册管理类  可以按顺序注册filter
     *                并且可以用setOrder()方法设置排序值
     *
     * @Date:2018/4/27 10:08
     *
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    /**
     * @Author:zms
     *
     * @Description:shiro生命周期管理器 负责调用destoryable方法
     *
     *
     * @Date:2018/4/27 10:13
     *
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    
    /**
     * @Author:zms
     *
     * @Description:开启shiro aop支持
     *
     * @Date:2018/4/27 12:01
     *
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }
    /**
     * @Author:zms
     *
     * @Description:支持shiro注解
     *
     * @Date:2018/4/27 12:03
     *
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * @Author:zms
     *
     * @Description:CAS过滤器
     *
     * @Date:2018/4/27 12:04
     *
     */
    @Bean(name = "casFilter")
    public CasFilter getCasFilter() {
        CasFilter casFilter = new CasFilter();
        casFilter.setName("casFilter");
        casFilter.setEnabled(true);
        // 登录失败后跳转的URL，也就是 Shiro 执行 CasRealm 的 doGetAuthenticationInfo 方法向CasServer验证tiket
        casFilter.setFailureUrl(loginUrl);// 我们选择认证失败后再打开登录页面
        casFilter.setLoginUrl(loginUrl);
        return casFilter;
    }


    /**
     * @Author:zms
     *
     * @Description:创建shiroFilter
     *
     * @Date:2018/4/27 12:01
     *
     */
    @Bean(name="shiroFilter")
    public ShiroFilterFactoryBean shirFilter(DefaultWebSecurityManager  securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        // 如果设置的话 登录成功会跳转到指定的页面 而不会跳转到登录前页面
        // 后台管理的话建议使用 其他的用途不建议
        // shiroFilterFactoryBean.setSuccessUrl("xxx");
        // 添加casFilter到shiroFilter中，注意，casFilter需要放到shiroFilter的前面，
        // 从而保证程序在进入shiro的login登录之前就会进入单点认证
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("casFilter", getCasFilter());
        // logout已经被单点登录的logout取代
        // filters.put("logout",logoutFilter());
        shiroFilterFactoryBean.setFilters(filters);
        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }

    /**
     * 加载shiroFilter权限控制规则（从数据库读取然后配置）,角色/权限信息由MyShiroCasRealm对象提供doGetAuthorizationInfo实现获取来的
     * 生产中会将这部分规则放到数据库中
     * @param shiroFilterFactoryBean
     */

    private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean){
        /////////////////////// 下面这些规则配置最好配置到配置文件中，注意，此处加入的filter需要保证有序，所以用的LinkedHashMap ///////////////////////
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

        filterChainDefinitionMap.put(casFilterUrlPattern, "casFilter");

        //2.不拦截的请求
        filterChainDefinitionMap.put("/login", "anon");
        // 此处将logout页面设置为anon，而不是logout，因为logout被单点处理，而不需要再被shiro的logoutFilter进行拦截
        filterChainDefinitionMap.put("/logout","anon");
        filterChainDefinitionMap.put("/error","anon");
        //3.拦截的请求（从本地数据库获取或者从casserver获取(webservice,http等远程方式)，看你的角色权限配置在哪里）
        filterChainDefinitionMap.put("/user/**", "anon"); //需要登录

        //4.登录过的不拦截
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }
    /**
     * @Author:zms
     *
     * @Description:自定义角色匹配
     *
     * @Date:2018/4/27 15:39
     *
     */
    @Bean(name="rolePermissionResolver")
    public MyRolePermissionResolver getRolePermission(){
        MyRolePermissionResolver myRolePermissionResolver=new MyRolePermissionResolver();
        return myRolePermissionResolver;
    }
    /**
     * @Author:zms
     *
     * @Description:自定义权限匹配
     *
     * @Date:2018/4/27 15:40
     *
     */
    @Bean(name="permissionResolver")
    public BitAndWildPermissionResolver getPermission(){
        BitAndWildPermissionResolver bitAndWildPermissionResolver=new BitAndWildPermissionResolver();
        return bitAndWildPermissionResolver;
    }

    /**
     * @Author:zms
     *
     * @Description:自定义角色权限匹配解析器
     *
     * @Date:2018/4/27 15:40
     *
     */
    @Bean(name="authorizer")
    public ModularRealmAuthorizer getModular(){
        ModularRealmAuthorizer modularRealmAuthorizer=new ModularRealmAuthorizer();
        modularRealmAuthorizer.setRolePermissionResolver(getRolePermission());
        modularRealmAuthorizer.setPermissionResolver(getPermission());
        return modularRealmAuthorizer;
    }




}
