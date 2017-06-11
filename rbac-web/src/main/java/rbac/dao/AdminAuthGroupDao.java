package rbac.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rbac.dao.repository.AdminAuthGroup;

import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/17
 * @note: 请补充说明
 * @history:
 */
@Repository
public interface AdminAuthGroupDao extends PagingAndSortingRepository<AdminAuthGroup, Long>, JpaSpecificationExecutor<AdminAuthGroup> {

    AdminAuthGroup findByUuid(String uuid);

    AdminAuthGroup findByTitle(String title);

    List<AdminAuthGroup> findByIdIn(List<Long> ids);
}
