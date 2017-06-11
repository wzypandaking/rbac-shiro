package rbac.web.param;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/18
 * @note: 请补充说明
 * @history:
 */
@Setter
@Getter
public class AdminGroupUserAddParam {

    private String groupUuid;

    private List<String> userUuid;
}
