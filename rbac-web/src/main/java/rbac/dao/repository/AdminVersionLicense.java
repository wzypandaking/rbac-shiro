package rbac.dao.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/19
 * @note: 请补充说明
 * @history:
 */
@Entity
@Table(name = "admin_version_license")
@Setter
@Getter
public class AdminVersionLicense extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7925284700759452541L;

    @Column(name = "version_id", updatable = false)
    private Long versionId;

    @Column(name = "status")
    private Integer status;

    @Column(name="add_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addTime; //  创建时间

    @Column(name="expire_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireTime; //  创建时间

    @Column(name="license")
    private String license; //  版本license'

    @Column(name = "public_key", updatable = false)
    private String publicKey;

    @Column(name = "private_key", updatable = false)
    private String privateKey;
}
