package rbac.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rbac.dao.repository.AdminAdminNav;

import java.io.Serializable;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/17
 * @note: 请补充说明
 * @history:
 */
public interface AdminAdminNavDao extends PagingAndSortingRepository<AdminAdminNav, Long>, JpaSpecificationExecutor<AdminAdminNav> {

    AdminAdminNav findByUuid(String uuid);

    Integer countByPid(Long pid);

    AdminAdminNav findById(Long id);
}
