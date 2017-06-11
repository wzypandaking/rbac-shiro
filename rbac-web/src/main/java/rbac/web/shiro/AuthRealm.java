package rbac.web.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;
import org.springframework.web.servlet.support.RequestContextUtils;
import rbac.dao.AdminUsersDao;
import rbac.dao.repository.AdminUsers;
import rbac.service.AdminUsersService;
import rbac.utils.Result;
import rbac.web.lang.AdminUsersLang;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

/**
 * Created by pandaking on 2017/5/25.
 */
@Slf4j
public class AuthRealm extends AuthorizingRealm {

//    @Autowired
//    private AdminUsersDao adminUsersDao;
//    @Autowired
//    private AdminUsersService adminUsersService;

    /**
     * 登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return new SimpleAuthenticationInfo(new AdminUsers(), "aaa", this.getClass().getName());
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        String username = token.getUsername();
//        AdminUsers adminUsers = adminUsersDao.findByUsername(username);
//        Result result = adminUsersService.checkUser(adminUsers);
//        if (!result.isSuccess()) {
//            throw new AuthenticationException(result.getMessage());
//        }
//        return new SimpleAuthenticationInfo(adminUsers, adminUsers.getPassword(), this.getClass().getName());
    }

    /**
     * 给登录用户授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Set<String> rules = (Set<String>) request.getSession().getAttribute("rules");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(rules);
//        AdminUsers users = (AdminUsers) principalCollection.fromRealm(this.getClass().getName()).iterator().next();
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        Result<Set<String>> result = adminUsersService.getRules(users.getUuid());
//        if (!result.isSuccess()) {
//            return info;
//        }
//        info.addStringPermissions(result.getData());
//
        return info;
    }
}
