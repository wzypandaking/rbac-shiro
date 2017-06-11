package rbac.web.shiro;

import org.apache.shiro.authz.annotation.Logical;
import rbac.RbacPermissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pandaking on 2017/5/27.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {

    RbacPermissions[] value();

    Logical logical() default Logical.AND;

}
