package rbac.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rbac.dao.repository.AdminAuthRule;

import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/17
 * @note: 请补充说明
 * @history:
 */
@Repository
public interface AdminAuthRuleDao extends PagingAndSortingRepository<AdminAuthRule, Long>, JpaSpecificationExecutor<AdminAuthRule> {

    AdminAuthRule findById(Long id);

    AdminAuthRule findByUuid(String uuid);

    Integer countByPid(Long pid);

    List<AdminAuthRule> findByIdIn(List<Long> ids);

    List<AdminAuthRule> findByUuidIn(List<String> ids);
}
