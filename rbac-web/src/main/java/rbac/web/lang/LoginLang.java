package rbac.web.lang;

import lombok.Getter;
import rbac.utils.Lang;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
@Getter
public enum LoginLang implements Lang {

    SUCCESS(true, "001", "登录成功"),
    FAILED(false, "002", "用户名或密码错误"),
    LOGOUT_SUCCESS(true, "003", "成功退出"),
    LOGIN_FIRST(true, "004", "您还未登录"),

    ;

    private boolean success;

    private String code;

    private String message;

    LoginLang(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return String.format("%s%s", "login", code);
    }
}
