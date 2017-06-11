package rbac.utils;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
public class BeanUtil {

    public static <S, T> T copy(S s, Class<T> t) {
        return JSON.parseObject(JSON.toJSONString(s), t);
    }

    /**
     *
     * @param s
     * @param type  new com.alibaba.fastjson.TypeReference<T>(){}.getType()
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T copy(S s, Type type) {
        return JSON.parseObject(JSON.toJSONString(s), type);
    }
}
