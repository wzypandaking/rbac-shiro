package rbac.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rbac.dao.repository.AdminAuthGroupAccess;

import java.util.Collection;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/17
 * @note: 请补充说明
 * @history:
 */
@Repository
public interface AdminAuthGroupAccessDao extends PagingAndSortingRepository<AdminAuthGroupAccess, Long>, JpaSpecificationExecutor<AdminAuthGroupAccess> {

    List<AdminAuthGroupAccess> findByGroupId(Long groupId);

    Integer deleteByGroupId(Long groupId);

    List<AdminAuthGroupAccess> findByUid(Long uid);

    AdminAuthGroupAccess findByUidAndGroupId(Long uid, Long groupId);
}
