
package com.it.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.it.util.CustomConfiguration;
import com.it.util.ItdragonUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro 配置，重点
 *
 * @author itdragon
 */

@Configuration

public class ShiroSpringConfig {

    private static final transient Logger log = LoggerFactory.getLogger(ShiroSpringConfig.class);
    private static final CustomConfiguration CustomConfiguration = new CustomConfiguration();


    /**
     * 配置拦截器
     * <p>
     * 定义拦截URL权限，优先级从上到下
     * 1). anon  : 匿名访问，无需登录
     * 2). authc : 登录后才能访问
     * 3). logout: 登出
     * 4). roles : 角色过滤器
     * <p>
     * URL 匹配风格
     * 1). ?：匹配一个字符，如 /admin? 将匹配 /admin1，但不匹配 /admin 或 /admin/；
     * 2). *：匹配零个或多个字符串，如 /admin* 将匹配 /admin 或/admin123，但不匹配 /admin/1；
     * 2). **：匹配路径中的零个或多个路径，如 /admin/** 将匹配 /admin/a 或 /admin/a/b
     * <p>
     * 配置身份验证成功，失败的跳转路径
     */

    @Bean
    public ShiroFilterFactoryBean shirFilter(DefaultWebSecurityManager securityManager) {
        log.info("^^^^^^^^^^^^^^^^^^^^ ITDragon 配置Shiro拦截工厂");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 静态资源匿名访问
        filterChainDefinitionMap.put("/resource/**", "anon");
        //img
        filterChainDefinitionMap.put("/image/**", "anon");
        //css
        filterChainDefinitionMap.put("/css/**", "anon");
        //js
        filterChainDefinitionMap.put("/js/**", "anon");
        //前台注册界面匿名访问
        filterChainDefinitionMap.put("/prosceniumReg", "anon");
        //后台注册页面匿名访问
        filterChainDefinitionMap.put("/backstageReg", "anon");
        //后台登录页面匿名访问
        filterChainDefinitionMap.put("/backstageLogin", "anon");
        //注册接口匿名访问
        filterChainDefinitionMap.put("/user/userRegister.do", "anon");
        //忘记密码页面
        filterChainDefinitionMap.put("/forgetPas", "anon");
        //发送密码接口
        filterChainDefinitionMap.put("/retrievePass", "anon");
        //前台首页界面
        filterChainDefinitionMap.put("/diplomaProject", "anon");
        //车辆列表界面
        filterChainDefinitionMap.put("/productList", "anon");
        //车辆列表加载
        filterChainDefinitionMap.put("/product/product.do", "anon");
        filterChainDefinitionMap.put("/product/getCount.do", "anon");
        //车辆详情
        filterChainDefinitionMap.put("/productDetails", "anon");
        //收藏
        filterChainDefinitionMap.put("/product/collect.do", "anon");
        //收藏
        filterChainDefinitionMap.put("/product/collect.do", "anon");
        //资讯
        filterChainDefinitionMap.put("/articleListPage", "anon");
        //资讯详情
        filterChainDefinitionMap.put("/articleDetailsPage", "anon");
        //搜索
        filterChainDefinitionMap.put("/search", "anon");
        // 登录接口匿名访问
        filterChainDefinitionMap.put("/login.do", "anon");
        filterChainDefinitionMap.put("/loginPro.do", "anon");
        // 用户退出，只需配置logout即可实现该功能
        //filterChainDefinitionMap.put("/logout.do", "logout");
        // 其他路径均需要身份认证，一般位于最下面，优先级最低
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // shiro拦截未登录用户后需要访问跳转登录页面的接口
        shiroFilterFactoryBean.setLoginUrl("/loginShiro");
        // shiro认证成功后会跳转的接口（登录成功后跳转的路径）
        shiroFilterFactoryBean.setSuccessUrl("/");
        // 验证失败后跳转的路径
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        return shiroFilterFactoryBean;
    }


    /**
     * 配置Shiro生命周期处理器
     */

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * 自动创建代理类，若不添加，Shiro的注解可能不会生效。
     */

    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }


    /**
     * 开启Shiro的注解
     */

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 配置自定义参数设置
     */
    @Bean
    public CustomConfiguration setIsEncrypted() {
        CustomConfiguration.setIsEncrypted("no");
        //alipay (支付宝支付) balance (余额付款) simulation(模拟支付)
        CustomConfiguration.setPayType("simulation");
        return CustomConfiguration;
    }

    /**
     * 配置加密匹配，使用MD5的方式，进行1024次加密
     */

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(1024);
        return hashedCredentialsMatcher;
    }


    /**
     * 自定义Realm，可以多个
     */

    @Bean
    public ITDragonShiroRealm itDragonShiroRealm() {
        ITDragonShiroRealm itDragonShiroRealm = new ITDragonShiroRealm();
        //判断用户密码是否加密，是，则设置加密参数
        if (ItdragonUtils.isEncrypted()) {
            itDragonShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        }

        return itDragonShiroRealm;
    }


    /**
     * SecurityManager 安全管理器；Shiro的核心
     */

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(itDragonShiroRealm());
        return securityManager;
    }

    /**
     * Shiro和thymeleaf标签依赖
     *
     * @return
     */

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                //单位为S
                container.setSessionTimeout(18000);
                ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/400.html");
                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");
                container.addErrorPages(error400Page, error401Page, error404Page, error500Page);
            }
        };
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
