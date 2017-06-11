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
public enum AdminDepartmentLang implements Lang {


    ADD_SUCCESS(true, "001", "创建成功"),
    NOT_FOUND(false, "002", "数据不存在"),
    DELETE_SUCCESS(true, "003", "删除成功"),
    EDIT_SUCCESS(true, "004", "编辑成功"),
    DELETE_FAILED_HAS_CHILD(false, "005", "删除失败，该节点包含子节点"),
    PARAM_ERROR(false, "006", "参数错误"),

    ;

    private boolean success;

    private String code;

    private String message;

    AdminDepartmentLang(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return String.format("%s%s", "admindepartment", code);
    }
}
