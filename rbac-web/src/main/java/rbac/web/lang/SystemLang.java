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
public enum SystemLang implements Lang {

    ERROR(false, "001", "系统内部错误"),
    SUCCESS(true, "002", "执行成功"),
    PARAM_WRONG(false, "003", "参数不正确"),
    ;

    private boolean success;

    private String code;

    private String message;

    SystemLang(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return String.format("%s%s", "system", code);
    }
}
