package rbac.utils;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
public class Result<D> implements Serializable {
    private static final long serialVersionUID = -8796134807481732635L;

    private static final String SUCCESS_CODE = "00000000";
    @Getter
    private D data;
    @Getter
    private boolean success;
    @Getter
    private String code;
    @Getter
    private String message;

    @Getter
    public enum  Empty implements Lang {

        EMPTY(true, "", "");

        private boolean success;

        private String code;

        private String message;

        Empty(boolean success, String code, String message) {
            this.success = success;
            this.code = code;
            this.message = message;
        }
    }

    public static <D> Result<D> wrapResult(D data) {
        return wrapResult(data, Empty.EMPTY);
    }

    public static <T> Result<T> wrapResult(T data, Lang lang) {
        Result<T> result = new Result<>();
        result.data = data;
        result.success = lang.isSuccess();
        result.code = lang.getCode();
        result.message = lang.getMessage();
        return result;
    }

    public static <T> Result<T> wrapErrorResult(String message) {
        Result<T> result = new Result<>();
        result.data = null;
        result.success = false;
        result.code = "";
        result.message = message;
        return result;
    }

    public static <D> Result<D> wrapResult(Lang lang) {
        Result<D> result = new Result<>();
        result.success = lang.isSuccess();
        result.code = lang.getCode();
        result.message = lang.getMessage();
        return result;
    }
}
