package org.springframework.rbac.web;


import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.rbac.shiro.RestAuthRealm;
import org.springframework.rbac.shiro.RestCredentialsMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.DelegatingFilterProxy;
import rbac.thymeleaf.shiro.dialect.ShiroDialect;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by pandaking on 2017/5/25.
 */
public abstract class ShiroConfigurer {

    /**
     * 参考：http://shiro.apache.org/web.html#Web-DefaultFilters
     * @return
     */
    public abstract LinkedHashMap<String, String> filterChainDefinitionMap();

    public abstract String getLoginUrl();
    public abstract String getSuccessUrl();

    public Map<String, Filter> filterMap() {
        return Collections.EMPTY_MAP;
    }

    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        bean.setFilter(proxy);
        return bean;
    }

    /**
     *
     * <filter>
     *      <filter-name>ShiroFilter</filter-name>
     *      <filter-class>org.apache.shiro.web.servlet.ShiroFilter</filter-class>
     * </filter>
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager manager) {
        ShiroFilterFactoryBean factory = new ShiroFilterFactoryBean();
        factory.setSecurityManager(manager);
        factory.setLoginUrl(getLoginUrl());
        factory.setSuccessUrl(getSuccessUrl());
        factory.setFilterChainDefinitionMap(filterChainDefinitionMap());
        Map<String, Filter> filterMap = filterMap();
        if (!CollectionUtils.isEmpty(filterMap)) {
            factory.setFilters(filterMap());
        }
        return factory;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(AuthorizingRealm authRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        return manager;
    }

    @Bean("authRealm")
    public AuthorizingRealm authRealm(@Qualifier("credentialsMatcher")SimpleCredentialsMatcher matcher) {
        RestAuthRealm authRealm = new RestAuthRealm();
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }

    @Bean("credentialsMatcher")
    public SimpleCredentialsMatcher credentialsMatcher() {
        return new RestCredentialsMatcher();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}
