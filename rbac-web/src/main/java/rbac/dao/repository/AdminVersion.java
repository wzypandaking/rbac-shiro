package rbac.dao.repository;

import lombok.Getter;
import lombok.Setter;
import rbac.utils.RandomUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/18
 * @note: 请补充说明
 * @history:
 */
@Entity
@Table(name = "admin_version")
@Setter
@Getter
public class AdminVersion extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4729533628134297882L;

    @Column(name="title")
    private String title;   //  版本名称'

    @Column(name="content")
    private String content; //  版本协议

    @Column(name="add_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addTime; //  创建时间

}
