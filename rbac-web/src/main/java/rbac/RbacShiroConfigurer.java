package rbac;


import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.rbac.web.ShiroConfigurer;
import rbac.web.shiro.AuthRealm;
import rbac.web.shiro.CredentialsMatcher;
import rbac.web.shiro.RbacFormAuthenticationFilter;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by pandaking on 2017/5/25.
 */
@Configuration
public class RbacShiroConfigurer extends ShiroConfigurer {

    @Override
    public LinkedHashMap<String, String> filterChainDefinitionMap() {
        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
//        filterChainDefinitionMap.put("/admin/login", "anon"); //表示可以匿名访问
//        filterChainDefinitionMap.put("/auth/check", "anon"); //表示可以匿名访问
        //  静态资源
        {
            filterChainDefinitionMap.put("/css/**", "anon"); //表示可以匿名访问
            filterChainDefinitionMap.put("/fonts/**", "anon"); //表示可以匿名访问
            filterChainDefinitionMap.put("/images/**", "anon"); //表示可以匿名访问
            filterChainDefinitionMap.put("/js/**", "anon"); //表示可以匿名访问
            filterChainDefinitionMap.put("/layui/**", "anon"); //表示可以匿名访问
            filterChainDefinitionMap.put("/**.js", "anon"); //表示可以匿名访问
        }
        filterChainDefinitionMap.put("/*", "authc");//表示需要认证才可以访问
        filterChainDefinitionMap.put("/**", "authc");//表示需要认证才可以访问
        filterChainDefinitionMap.put("/*.*", "authc");
        return filterChainDefinitionMap;
    }

    @Override
    public String getLoginUrl() {
        return "/signin.html";
    }

    @Override
    public String getSuccessUrl() {
        return "/index.html";
    }

    @Override
    public Map<String, Filter> filterMap() {
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("authc", new RbacFormAuthenticationFilter());
        return filterMap;
    }

    @Override
    public AuthorizingRealm authRealm(@Qualifier("credentialsMatcher") org.apache.shiro.authc.credential.CredentialsMatcher matcher) {
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }

    @Override
    public SimpleCredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher();
    }
}
