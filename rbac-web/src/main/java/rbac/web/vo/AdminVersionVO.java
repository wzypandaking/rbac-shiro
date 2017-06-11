package rbac.web.vo;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/18
 * @note: 请补充说明
 * @history:
 */
@Setter
@Getter
public class AdminVersionVO {

    private String uuid;

    private String title;

    private Date addTime;

    public String getAddTime() {
        return new DateTime(addTime).toString("yyyy-MM-dd HH:mm:ss");
    }
}
