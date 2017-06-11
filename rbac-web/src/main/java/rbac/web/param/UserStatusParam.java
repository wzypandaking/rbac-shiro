package rbac.web.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/16
 * @note: 请补充说明
 * @history:
 */
@Setter
@Getter
public class UserStatusParam {

    private String uuid;

    private Integer status;
}
