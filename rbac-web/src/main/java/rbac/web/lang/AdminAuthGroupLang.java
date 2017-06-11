package rbac.web.lang;

import lombok.Getter;
import rbac.utils.Lang;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/16
 * @note: 请补充说明
 * @history:
 */
@Getter
public enum AdminAuthGroupLang implements Lang {

    DROP_SUCCESS(true, "001", "删除成功"),
    GROUP_EXISTS(false, "002", "用户组已经存在"),
    ADD_SUCCESS(true, "003", "添加成功"),
    GROUP_NOT_EXISTS(false, "004", "用户组不存在"),
    MODIFY_RULES_SUCCESS(true, "005", "权限赋予成功"),
    MODIFY_SUCCESS(true, "006", "修改成功"),
    HAS_NO_RULES(false, "007", "该用户组没有配置权限"),
    ;

    private boolean success;

    private String code;

    private String message;

    AdminAuthGroupLang(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return String.format("%s%s", "adminauthgroup", code);
    }
}
