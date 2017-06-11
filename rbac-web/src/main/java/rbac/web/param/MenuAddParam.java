package rbac.web.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/17
 * @note: 请补充说明
 * @history:
 */
@Setter
@Getter
public class MenuAddParam {

    private String name;

    private String mca;

    private String ico;

    private Integer orderNumber;

    private String pUuid;

    private String ruleUuid;
}
