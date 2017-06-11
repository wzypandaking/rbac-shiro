package rbac.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import rbac.RbacPermissions;
import rbac.thymeleaf.shiro.dialect.ShiroFacade;
import rbac.utils.AdministratorUtil;
import rbac.web.shiro.RequiresPermissions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by pandaking on 2017/5/27.
 */
@Slf4j
public class RbacPermissionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (AdministratorUtil.isSuper()) {
            return true;
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;
        RequiresPermissions requiresPermissions = method.getMethodAnnotation(RequiresPermissions.class);
        if (requiresPermissions == null) {
            return true;
        }
        RbacPermissions[] permissionList = requiresPermissions.value();
        if (permissionList.length == 0) {
            return true;
        }
        String[] permissions = new String[permissionList.length];
        for (int i = 0; i < permissionList.length; i ++) {
            permissions[i] = permissionList[i].getName();
        }
        if (requiresPermissions.logical() == Logical.AND) {
            return ShiroFacade.hasAllPermissions(permissions);
        } else if (requiresPermissions.logical() == Logical.OR) {
            return ShiroFacade.hasAnyPermissions(permissions);
        }
        return false;
    }

}