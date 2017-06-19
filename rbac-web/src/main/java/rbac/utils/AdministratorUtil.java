package rbac.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Set;

/**
 * Created by pandaking on 2017/6/5.
 */
public class AdministratorUtil {

    public static boolean isSuper() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getSession().getAttribute("id") != null && (Long) request.getSession().getAttribute("id") == 1;
    }

    public static Set<String> getRules() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();;
        if (request.getSession().getAttribute("id") == null) {
            return Collections.EMPTY_SET;
        }
        return (Set<String>) request.getSession().getAttribute("rules");
    }

    public static Set<Long> getManagedUids() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();;
        if (request.getSession().getAttribute("id") == null) {
            return Collections.EMPTY_SET;
        }
        return (Set<Long>) request.getSession().getAttribute("managedUids");
    }
}
