package rbac.web.aop;

import com.google.common.collect.ImmutableList;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rbac.dao.repository.BaseEntity;
import rbac.utils.AdministratorUtil;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by pandaking on 2017/6/2.
 */
@Aspect
@Configuration
public class JpaDaoAspect {

    private final String filterName = "DepartmentFilter";

    private List<String> excludeURI = ImmutableList.of(
            "admin/login",
            "admin/user",
            "menu/show"
    ).asList();

    @Autowired
    private EntityManager entityManager;

    @Around("execution(* rbac.dao.*Dao.save(..))")
    public Object save(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Long id = (Long)request.getSession().getAttribute("id");
        Object object = pjp.getArgs()[0];
        if(object instanceof Collection) {
            Iterator iterator = ((Collection) object).iterator();
            while (iterator.hasNext()) {
                BaseEntity entity = (BaseEntity) iterator.next();
                entity.setCreator(id);
                entity.setEditor(id);
            }
        } else if (object instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) object;
            entity.setCreator(id);
            entity.setEditor(id);
        } else {
            throw new RuntimeException("没有继承 BaseEntity.java");
        }
        return pjp.proceed(pjp.getArgs());
    }

    @Around("execution(* rbac.dao.*Dao.*(..)) && !execution(* rbac.dao.*Dao.save(..))")
    public Object repositoryPointcut(ProceedingJoinPoint pjp) throws Throwable {
        return process(pjp);
    }

    private Object process(ProceedingJoinPoint pjp) throws Throwable {
        if (AdministratorUtil.isSuper()) {
            return pjp.proceed();
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        for (String item : excludeURI) {
            if (request.getRequestURI().contains(item)) {
                return pjp.proceed();
            }
        }

        Set<Long> managedUids = (Set<Long>) request.getSession().getAttribute("managedUids");
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter(filterName);
        filter.setParameterList("creator", managedUids);
        Object obj  = pjp.proceed();
        session.disableFilter(filterName);
        return obj;
    }


}
