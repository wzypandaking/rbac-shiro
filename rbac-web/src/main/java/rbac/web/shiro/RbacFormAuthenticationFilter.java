package rbac.web.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import rbac.utils.AdministratorUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pandaking on 2017/6/4.
 */
public class RbacFormAuthenticationFilter extends FormAuthenticationFilter {

    private List<String> excludeURI = new ArrayList<>();

    public RbacFormAuthenticationFilter() {
        excludeURI.add("/admin/login");
        excludeURI.add("/client/api/auth/check");
    }


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (AdministratorUtil.isSuper()) {
            return true;
        }
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        for (String item : excludeURI) {
            if (requestURI.contains(item)) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
