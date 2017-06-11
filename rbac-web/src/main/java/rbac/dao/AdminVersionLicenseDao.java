package rbac.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import rbac.dao.repository.AdminVersionLicense;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/19
 * @note: 请补充说明
 * @history:
 */
@Repository
public interface AdminVersionLicenseDao extends PagingAndSortingRepository<AdminVersionLicense, Long>, JpaSpecificationExecutor<AdminVersionLicense> {

    AdminVersionLicense findByUuid(String uuid);

    AdminVersionLicense findByLicense(String licenseKey);
}
