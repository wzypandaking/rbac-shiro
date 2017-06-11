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
@Table(name = "admin_department_user")
@Getter
@Setter
public class AdminDepartmentUser extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -2991491816306350983L;
    // 用户id
    @Column(name = "uid")
    private Long uid;

    // 用户uuid
    @Column(name = "user_uuid")
    private String userUuid;

    // 部门id
    @Column(name = "department_id")
    private Long departmentId;

    // 部门id
    @Column(name = "department_uuid")
    private String departmentUuid;



}
