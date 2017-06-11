package rbac.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rbac.dao.repository.AdminDepartmentUser;

import java.util.Collection;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/17
 * @note: 请补充说明
 * @history:
 */
@Repository
public interface AdminDepartmentUserDao extends PagingAndSortingRepository<AdminDepartmentUser, Long>, JpaSpecificationExecutor<AdminDepartmentUser> {

    List<AdminDepartmentUser> findUidByDepartmentUuid(String departmentUuid);

    List<AdminDepartmentUser> findUserUuidByDepartmentIdIn(Collection departmentIds);

    List<AdminDepartmentUser>  findDepartmentIdByUid(Long uid);

    List<AdminDepartmentUser>  findDepartmentIdByUserUuid(String userUuid);

    Integer deleteByDepartmentId(Long departmentId);
}
