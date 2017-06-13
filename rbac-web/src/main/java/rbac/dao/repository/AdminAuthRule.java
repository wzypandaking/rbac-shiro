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
@Table(name = "admin_auth_rule")
@Getter
@Setter
public class AdminAuthRule extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3731709563997649507L;


    @Column(name = "pid", updatable = false)
    private Long pid = 0L;  // 父级id

    @Column(name = "name", updatable = false)
    private String name; // 规则唯一标识

    @Column(name = "title")
    private String title;    // 规则中文名称

}
