package rbac.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import rbac.dao.repository.AdminUsers;

import java.util.Collection;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
public interface AdminUsersDao extends PagingAndSortingRepository<AdminUsers, Long>, JpaSpecificationExecutor<AdminUsers> {

    AdminUsers findById(Long id);

    AdminUsers findByUsername(String username);

    AdminUsers findByUuid(String uuid);

    List<AdminUsers> findByUuidIn(Collection<String> uuid);

    AdminUsers findByEmail(String email);

    AdminUsers findByPhone(String phone);
}
