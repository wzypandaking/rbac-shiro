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
public enum AdminAuthRuleLang implements Lang {

    DROP_SUCCESS(true, "001", "删除成功"),
    NOT_FOUND(false, "002", "没有找到相应的权限值"),
    SAVE_SUCCESS(true, "003", "保存成功"),
    PARENT_NOT_FOUND(false, "004", "没有找到父级信息"),
    DELETE_FAILED_HAS_CHILD(false, "004", "删除失败，请先清理子权限"),
    ;

    private boolean success;

    private String code;

    private String message;

    AdminAuthRuleLang(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return String.format("%s%s", "adminauthrule", code);
    }
}
