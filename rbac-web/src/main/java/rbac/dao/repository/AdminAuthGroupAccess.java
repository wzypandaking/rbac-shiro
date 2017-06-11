package rbac.dao.repository;

import lombok.Getter;
import lombok.Setter;
import rbac.utils.RandomUtil;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/16
 * @note: 请补充说明
 * @history:
 */
@Entity
@Table(name = "admin_auth_group_access")
@Getter
@Setter
public class AdminAuthGroupAccess extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 4612628924112400481L;
    @Column(name = "uid")
    private Long uid;

    @Column(name = "group_id")
    private Long groupId;
}
