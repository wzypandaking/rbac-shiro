package rbac.utils;

import java.io.Serializable;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
public interface Lang extends Serializable {

    boolean isSuccess();

    String getCode();

    String getMessage();
}

