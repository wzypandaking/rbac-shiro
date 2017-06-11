package rbac.utils;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
public class PagingResult<T> implements Serializable {

    private static final long serialVersionUID = 8911072786251958689L;

    @Getter
    private Collection<T> list;
    @Getter
    private long total;
    @Getter
    private boolean success;
    @Getter
    private String code;
    @Getter
    private String message;
    @Getter
    private Integer page;
    @Getter
    private Integer size;

    public static <T> PagingResult<T> wrapResult(Collection<T> data, long total, Integer page, Integer size) {
        PagingResult<T> result = new PagingResult<>();
        result.list = data;
        result.total = total;
        result.success = true;
        result.page = page;
        result.size = size;
        return result;
    }

    public static <T> PagingResult<T> wrapResult(Lang lang) {
        PagingResult<T> result = new PagingResult<>();
        result.success = lang.isSuccess();
        result.code = lang.getCode();
        result.message = lang.getMessage();
        return result;
    }
}
