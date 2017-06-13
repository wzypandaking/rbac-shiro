package rbac.dao.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by pandaking on 2017/5/24.
 */
@Entity
@Table(name = "admin_department")
@Setter
@Getter
public class AdminDepartment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3273618787131061283L;
    //所属菜单
    @Column(name = "pid")
    private Long pid = 0l;

    //菜单名称
    @Column(name = "name")
    private String name;

}
