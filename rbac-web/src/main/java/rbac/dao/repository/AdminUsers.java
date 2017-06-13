package rbac.dao.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
@Entity
@Table(name = "admin_users")
@Setter
@Getter
public class AdminUsers extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 8245931790741156099L;
    //用户名',
    @Column(name = "username",  updatable = false)
    private String username;
    //登录密码；md5(md5(password) + salt),
    @Column(name = "password")
    private String password;
    //随机串',
    @Column(name = "salt")
    private String salt;
    //avatar目录',
    @Column(name = "avatar")
    private String avatar;
    //登录邮箱',
    @Column(name = "email")
    private String email;
    //激活码',
    @Column(name = "emailCode")
    private String emailCode;
    //手机号',
    @Column(name = "phone")
    private String phone;
    //；2：未验证',
    @Column(name = "status")
    private Integer status;
    //注册时间',
    @Column(name = "register_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerTime;
    //最后登录ip',
    @Column(name = "last_login_ip")
    private String lastLoginIp;
    //最后登录时间',
    @Column(name = "last_login_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginTime;

}
