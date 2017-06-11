package rbac.dao.repository;

import lombok.Getter;
import lombok.Setter;
import rbac.utils.RandomUtil;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by pandaking on 2017/5/24.
 */
@Entity
@Table(name = "admin_nav")
@Setter
@Getter
public class AdminAdminNav extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -4051337435449017480L;
    //所属菜单
    @Column(name = "pid", updatable = false)
    private Long pid = 0l;

    //菜单名称
    @Column(name = "name")
    private String name;

    //模块、控制器、方法
    @Column(name = "mca")
    private String mca;

    //awesome图标
    @Column(name = "ico")
    private String ico;

    //排序
    @Column(name = "order_number")
    private String orderNumber;

    //排序
    @Column(name = "rule")
    private String rule;

}
