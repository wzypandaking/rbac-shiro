package org.springframework.rbac.shiro;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by pandaking on 2017/5/31.
 */
@Setter
@Getter
public class AuthInfo {

    /**
     * 其他账户的UUID（包含自己的）
     * 当前用户可以查看到的数据权限
     */
    private List<String> uuid;

    /**
     * 当前用户拥有的权限
     */
    private List<String> permissions;


    private List<Menu> menus;

}
