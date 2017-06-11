package rbac.web.interceptor;

import com.alibaba.fastjson.JSON;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import rbac.utils.Result;
import rbac.web.lang.LoginLang;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
public class LoginInterceptor implements HandlerInterceptor {

    private List<String> excludeURI = new ArrayList<>();

    public LoginInterceptor() {
        excludeURI.add("/rbac/client");
        excludeURI.add("/rbac/admin/user");
        excludeURI.add("/rbac/signin.html");
        excludeURI.add("/rbac/admin/login");
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        for (String excludeUrl : excludeURI) {
            if (httpServletRequest.getRequestURI().contains(excludeUrl)) {
                return true;
            }
        }

        if (httpServletRequest.getSession().getAttribute("id") == null
                || (Long) httpServletRequest.getSession().getAttribute("id") <=0) {
            sendResponse(httpServletRequest, httpServletResponse);
            return false;
        }
        return true;
    }



    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private boolean sendResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (this.isAjax(request)) {
            response.setHeader("Content-type", MediaType.APPLICATION_JSON.toString());
            response.setCharacterEncoding("UTF-8");
            Result result = Result.wrapResult(LoginLang.LOGIN_FIRST);
            response.getWriter().write(JSON.toJSONString(result));
        } else {
            response.sendRedirect("/rbac/signin.html");
        }
        return false;
    }

    private boolean isAjax(HttpServletRequest request) {
        String HTTP_X_REQUESTED_WITH = request.getHeader("X-Requested-With");
        if(HTTP_X_REQUESTED_WITH != null && HTTP_X_REQUESTED_WITH.toLowerCase().equals("xmlhttprequest") ) {
            return true;
        }
        return false;
    }
}
