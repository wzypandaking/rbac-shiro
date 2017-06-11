package rbac.web.param;

import com.google.common.base.Splitter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
@Setter
@Getter
public class PageParam {

    private Integer page = 1;

    private Integer limit = 10;

    private String sort;

    public Integer getPage() {
        return page - 1;
    }

    public Sort getSort() {
        if (StringUtils.isBlank(sort)) {
            return null;
        }
        Iterator<String> iterable = Splitter.on(";").split(sort).iterator();
        List<Sort.Order> orders = new ArrayList<>();
        while (iterable.hasNext()) {
            String item = iterable.next();
            Iterator<String> sortItem = Splitter.on(":").split(item).iterator();
            String sortKey = sortItem.next();
            String sortDirection = sortItem.next().toLowerCase();
            Sort.Order order;
            switch (sortDirection) {
                case "asc":
                    order = new Sort.Order(Sort.Direction.ASC, sortKey);
                    break;
                case "desc":
                    order = new Sort.Order(Sort.Direction.DESC, sortKey);
                    break;
                default:
                    continue;
            }
            orders.add(order);
        }
        if (CollectionUtils.isEmpty(orders)) {
            return null;
        }
        return new Sort(orders);
    }
}
