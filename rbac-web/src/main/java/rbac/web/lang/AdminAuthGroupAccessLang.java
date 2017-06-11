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
public enum AdminAuthGroupAccessLang implements Lang {


    USER_UPDATE_SUCCESS(true, "001", "用户添加成功"),
    USER_HAS_NO_GROUP(false, "002", "用户没有分配用户组"),
    ;

    private boolean success;

    private String code;

    private String message;

    AdminAuthGroupAccessLang(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return String.format("%s%s", "adminauthgroupaccess", code);
    }
}
