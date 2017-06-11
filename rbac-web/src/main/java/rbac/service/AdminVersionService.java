package rbac.service;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import rbac.utils.RSAUtil;
import rbac.utils.Result;
import rbac.utils.SerializeUtil;
import rbac.web.lang.AdminVersionLang;
import rbac.web.lang.AdminVersionLicenseLang;
import rbac.web.param.PageParam;
import rbac.web.param.SearchParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.security.KeyPair;
import java.util.*;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/18
 * @note: 请补充说明
 * @history:
 */
@Component
public class AdminVersionService {

    private Logger logger = LoggerFactory.getLogger(AdminVersionService.class);

    @Autowired
    private AdminVersionDao adminVersionDao;
    @Autowired
    private AdminVersionLicenseDao adminVersionLicenseDao;


    public PagingResult<AdminVersion> search(SearchParam param, PageParam pageParam) {
        Page<AdminVersion> page = adminVersionDao.findAll(new Specification<AdminVersion>() {
            @Override
            public Predicate toPredicate(Root<AdminVersion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return null;
            }
        }, new PageRequest(pageParam.getPage(), pageParam.getLimit()));
        return PagingResult.wrapResult(page.getContent(), page.getTotalElements(), pageParam.getPage(), page.getSize());
    }

    /**
     * 添加版本
     * @param adminVersion
     * @return
     */
    public Result addVersion(AdminVersion adminVersion) {
        adminVersion.setAddTime(new Date());
        adminVersionDao.save(adminVersion);
        return Result.wrapResult(AdminVersionLang.ADD_SUCCESS);
    }

    /**
     * 给版本添加license，确定过期时间，license数量
     * @param version
     * @param expire
     * @param licenseNum
     * @return
     */
    @Transactional
    public Result addVersionLicense(AdminVersion version, Integer expire, Integer licenseNum) {
        List<AdminVersionLicense> licenses = new ArrayList<>(licenseNum);
        Date expireDate = DateTime.now().plusMonths(expire).toDate();
        Date now = new Date();
        String publicKey,privateKey;
        try {
            KeyPair keyPair = RSAUtil.generateKey();
            publicKey = Base64.getEncoder().encodeToString(SerializeUtil.serialize(keyPair.getPublic()));
            privateKey = Base64.getEncoder().encodeToString(SerializeUtil.serialize(keyPair.getPrivate()));
        } catch (Exception e) {
            logger.error("生成公钥私钥出现问题", e);
            throw new RuntimeException(e);
        }
        for (int i = 1; i <= licenseNum; i ++) {
            AdminVersionLicense adminVersionLicense = new AdminVersionLicense();
            adminVersionLicense.setLicense(UUID.randomUUID().toString());
            adminVersionLicense.setVersionId(version.getId());

            adminVersionLicense.setStatus(1);
            adminVersionLicense.setExpireTime(expireDate);
            adminVersionLicense.setAddTime(now);
            adminVersionLicense.setPublicKey(publicKey);
            adminVersionLicense.setPrivateKey(privateKey);
            licenses.add(adminVersionLicense);
        }
        adminVersionLicenseDao.save(licenses);
        return Result.wrapResult(AdminVersionLicenseLang.ADD_SUCCESS);
    }
}