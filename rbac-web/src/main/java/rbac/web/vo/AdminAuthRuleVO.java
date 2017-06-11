package rbac.web.vo;

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
public class AdminAuthRuleVO {

    private Long id;

    private String uuid;

    private Long pid;

    private String title;

    private String name;
}
