package rbac.web.vo;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
@Setter
@Getter
public class AdminUsersVO {

    private String uuid;

    private String username;

    private String avatar;

    private String email;

    private String phone;

    private Integer status;

    private Date registerTime;

    private String lastLoginIp;

    private Date lastLoginTime;

    public String getRegisterTime() {
        return new DateTime(registerTime).toString("yyyy-MM-dd HH:mm:ss");
    }

    public String getLastLoginTime() {
        if (lastLoginTime != null) {
            return new DateTime(lastLoginTime).toString("yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }
}
