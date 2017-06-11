package rbac.service;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rbac.dao.AdminVersionDao;
import rbac.dao.AdminVersionLicenseDao;
import rbac.dao.repository.AdminVersion;
import rbac.dao.repository.AdminVersionLicense;
import rbac.utils.PagingResult;
import rbac.utils.Result;
import rbac.web.lang.AdminVersionLang;
import rbac.web.lang.AdminVersionLicenseLang;
import rbac.web.lang.SystemLang;
import rbac.web.param.PageParam;
import rbac.web.param.SearchParam;
import rbac.web.param.VersionSearchParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/19
 * @note: 请补充说明
 * @history:
 */
@Component
public class AdminVersionLicenseService {

    @Autowired
    private AdminVersionLicenseDao adminVersionLicenseDao;
    @Autowired
    private AdminVersionDao adminVersionDao;

    /**
     * license列表
     * @param param
     * @param pageParam
     * @return
     */
    public PagingResult<AdminVersionLicense> search(final SearchParam param, PageParam pageParam) {
        VersionSearchParam p = (VersionSearchParam) param;
        AdminVersion adminVersion = null;
        if (StringUtils.isNotBlank(p.getVersionUuid())) {
            adminVersion = adminVersionDao.findByUuid(p.getVersionUuid());
            if (adminVersion == null) {
                return PagingResult.wrapResult(AdminVersionLang.NOT_FOUND);
            }
        }

        final AdminVersion version = adminVersion;
        Page<AdminVersionLicense> page = adminVersionLicenseDao.findAll(new Specification<AdminVersionLicense>() {
            @Override
            public Predicate toPredicate(Root<AdminVersionLicense> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (version != null) {
                    list.add(cb.equal(root.get("versionId"), version.getId()));
                }
                Predicate[] predicates = new Predicate[list.size()];
                list.toArray(predicates);
                return cb.and(predicates);
            }
        }, new PageRequest(pageParam.getPage(), pageParam.getLimit(), pageParam.getSort()));
        return PagingResult.wrapResult(page.getContent(), page.getTotalElements(), pageParam.getPage(), pageParam.getLimit());
    }

    /**
     * 切换license状态
     * @param license
     * @param status
     * @return
     */
    @Transactional
    public Result toggleStatus(AdminVersionLicense license, Integer status) {
        license.setStatus(status);
        adminVersionLicenseDao.save(license);
        return Result.wrapResult(AdminVersionLicenseLang.STATUS_TOGGLE_SUCCESS);
    }

    /**
     * 检查license是否有效
     * @param license
     * @return
     */
    public Result checkLicense(AdminVersionLicense license) {
        if (license == null) {
            return Result.wrapResult(AdminVersionLicenseLang.NOT_FOUND);
        }
        if (license.getStatus() == null || license.getStatus() != 1) {
            return Result.wrapResult(AdminVersionLicenseLang.ILLEGAL_STATUS);
        }
        if (DateTime.now().isAfter(license.getExpireTime().getTime())) {
            return Result.wrapResult(AdminVersionLicenseLang.LICENSE_HAD_EXPIRE);
        }
        return Result.wrapResult(SystemLang.SUCCESS);
    }
}
