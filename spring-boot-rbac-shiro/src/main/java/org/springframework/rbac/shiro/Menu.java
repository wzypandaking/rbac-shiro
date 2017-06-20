package org.springframework.rbac.shiro;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by pandaking on 2017/6/20.
 */
@Setter
@Getter
public class Menu {

    /**
     * 图标
     */
    private String ico;

    /**
     *  菜单名称
     */
    private String name;

    /**
     *  请求路径
     */
    private String mca;

    /**
     * 子菜单
     */
    List<Menu> child;

}

