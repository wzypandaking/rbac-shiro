//package org.springframework.rbac.shiro;
//
//import javafx.application.Application;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.catalina.core.ApplicationFilterChain;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.util.AntPathMatcher;
//import org.apache.shiro.util.PatternMatcher;
//import org.apache.shiro.web.filter.mgt.FilterChainManager;
//import org.apache.shiro.web.filter.mgt.FilterChainResolver;
//import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
//import org.apache.shiro.web.mgt.WebSecurityManager;
//import org.apache.shiro.web.servlet.AbstractShiroFilter;
//import org.apache.shiro.web.servlet.ProxiedFilterChain;
//import org.springframework.beans.factory.BeanInitializationException;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
///**
// * Created by pandaking on 2017/6/4.
// */
//@Slf4j
//public class RbacShiroFilterFactoryBean extends ShiroFilterFactoryBean {
//
//    @Override
//    public Class getObjectType() {
//        return RbacShiroFilterFactoryBean.RbasShiroFilter.class;
//    }
//
//    @Override
//    protected AbstractShiroFilter createInstance() throws Exception {
//        log.debug("Creating Shiro Filter instance.");
//        SecurityManager securityManager = this.getSecurityManager();
//        String msg;
//        if(securityManager == null) {
//            msg = "SecurityManager property must be set.";
//            throw new BeanInitializationException(msg);
//        } else if(!(securityManager instanceof WebSecurityManager)) {
//            msg = "The security manager does not implement the WebSecurityManager interface.";
//            throw new BeanInitializationException(msg);
//        } else {
//            FilterChainManager manager = this.createFilterChainManager();
//            PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
//            chainResolver.setFilterChainManager(manager);
//            return new RbacShiroFilterFactoryBean.RbasShiroFilter((WebSecurityManager)securityManager, chainResolver);
//        }
//    }
//
//    private final static class RbasShiroFilter extends AbstractShiroFilter {
//        protected RbasShiroFilter(WebSecurityManager webSecurityManager, FilterChainResolver resolver) {
//            if(webSecurityManager == null) {
//                throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
//            } else {
//                this.setSecurityManager(webSecurityManager);
//                if(resolver != null) {
//                    this.setFilterChainResolver(resolver);
//                }
//
//            }
//        }
//
//        @Override
//        protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
//            super.doFilterInternal(servletRequest, servletResponse, chain);
//        }
//
//        @Override
//        protected FilterChain getExecutionChain(ServletRequest request, ServletResponse response, FilterChain origChain) {
//            return super.getExecutionChain(request, response, origChain);
//        }
//
//        @Override
//        protected void executeChain(ServletRequest request, ServletResponse response, FilterChain origChain) throws IOException, ServletException {
//            super.executeChain(request, response, origChain);
//        }
//    }
//}
