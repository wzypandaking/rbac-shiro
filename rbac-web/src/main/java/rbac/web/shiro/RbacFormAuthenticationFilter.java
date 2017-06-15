package rbac.web.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
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
    private AntPathMatcher matcher = new AntPathMatcher();

    public RbacFormAuthenticationFilter() {
        excludeURI.add("/*/admin/**");
        excludeURI.add("/*/client/api/**");
    }


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (AdministratorUtil.isSuper()) {
            return true;
        }
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        for (String item : excludeURI) {
            if (matcher.match(item, requestURI)) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }
}
