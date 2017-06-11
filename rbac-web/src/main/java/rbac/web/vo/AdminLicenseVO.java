package rbac.web.vo;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/19
 * @note: 请补充说明
 * @history:
 */
@Setter
@Getter
public class AdminLicenseVO {

    private String uuid;

    private String license;

    public Integer status;

    private Date expireTime;

    private Date addTime;

    public String getExpireTime() {
        return new DateTime(expireTime).toString("yyyy-MM-dd HH:mm:ss");
    }

    public String getAddTime() {
        return new DateTime(addTime).toString("yyyy-MM-dd HH:mm:ss");
    }
}
