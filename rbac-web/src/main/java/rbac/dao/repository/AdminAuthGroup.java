package rbac.dao.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/16
 * @note: 请补充说明
 * @history:
 */
@Entity
@Table(name = "admin_auth_group")
@Setter
@Getter
public class AdminAuthGroup extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 799541128875161575L;

    @Column(name = "title")
    private String title;

    @Column(name = "status")
    private Integer status = 1;

    @Column(name = "rules")
    private String rules;

    @Column(name = "type")
    private Long type = 2L;
}
