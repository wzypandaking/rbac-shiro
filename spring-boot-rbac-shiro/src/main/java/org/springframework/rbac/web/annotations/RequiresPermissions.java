package org.springframework.rbac.web.annotations;

import org.apache.shiro.authz.annotation.Logical;
import org.springframework.rbac.Permissions;

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

    Permissions[] value();

    Logical logical() default Logical.AND;

}
