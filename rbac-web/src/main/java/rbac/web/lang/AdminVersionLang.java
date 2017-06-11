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
public enum AdminVersionLang implements Lang {

    ADD_SUCCESS(true, "001", "版本创建成功"),
    NOT_FOUND(false, "002", "版本不存在"),
    ;

    private boolean success;

    private String code;

    private String message;

    AdminVersionLang(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return String.format("%s%s", "adminversion", code);
    }
}
