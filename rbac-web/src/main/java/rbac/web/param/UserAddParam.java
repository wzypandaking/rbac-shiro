package rbac.web.param;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/16
 * @note: 请补充说明
 * @history:
 */
@Setter
@Getter
public class UserAddParam {

    private String username;

    private String password;

    private String avatar;

    private String email;

    private String phone;

    private Integer status;

    private Date registerTime;
}
