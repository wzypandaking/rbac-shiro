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
public enum AdminVersionLicenseLang implements Lang {

    ADD_SUCCESS(true, "001", "license创建成功"),
    NOT_FOUND(false, "002", "license不存在"),
    LICENSE_HAD_EXPIRE(false, "003", "license 已过期"),
    STATUS_TOGGLE_SUCCESS(true, "004", "license 状态切换成功"),
    ILLEGAL_STATUS(false, "005", "license 不可用"),

    ;

    private boolean success;

    private String code;

    private String message;

    AdminVersionLicenseLang(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return String.format("%s%s", "adminversionlicense", code);
    }
}
