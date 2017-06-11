package rbac.web.vo;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by pandaking on 2017/6/8.
 */
@Setter
@Getter
public class ProfileManageVO {

    private String username;

    private String email;

    private String phone;

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
