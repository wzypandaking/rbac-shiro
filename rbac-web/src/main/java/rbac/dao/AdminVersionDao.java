package rbac.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rbac.dao.repository.AdminVersion;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/18
 * @note: 请补充说明
 * @history:
 */
@Repository
public interface AdminVersionDao extends PagingAndSortingRepository<AdminVersion, Long>, JpaSpecificationExecutor<AdminVersion> {

    AdminVersion findByUuid(String uuid);
}
