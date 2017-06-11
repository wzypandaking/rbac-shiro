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
public enum  AdminUsersLang implements Lang {

    TOGGLE_STATUS_SUCCESS(true, "001", "操作成功"),
    NOT_FOUND(false, "0002", "用户不存在"),
    ADD_SUCCESS(true, "003", "账号创建成功"),
    LOGIN_FORBID(false, "004", "账号被禁止登录"),
    PASSWORD_CHECK_FAILED(false, "005", "两次输入的密码不一致"),
    PASSWORD_ERROR(false, "006", "原始密码不正确"),
    ;

    private boolean success;

    private String code;

    private String message;

    AdminUsersLang(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return String.format("%s%s", "adminusers", code);
    }
}
