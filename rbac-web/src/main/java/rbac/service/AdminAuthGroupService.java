package rbac.service;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import rbac.dao.AdminAuthGroupAccessDao;
import rbac.dao.AdminAuthGroupDao;
import rbac.dao.repository.AdminAuthGroup;
import rbac.dao.repository.AdminAuthGroupAccess;
import rbac.dao.repository.AdminAuthRule;
import rbac.utils.PagingResult;
import rbac.utils.Result;
import rbac.web.lang.AdminAuthGroupAccessLang;
import rbac.web.lang.AdminAuthGroupLang;
import rbac.web.lang.AdminAuthRuleLang;
import rbac.web.param.PageParam;
import rbac.web.param.SearchParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/17
 * @note: 请补充说明
 * @history:
 */
@Component
public class AdminAuthGroupService{

    @Autowired
    private AdminAuthGroupDao adminAuthGroupDao;
    @Autowired
    private AdminAuthGroupAccessDao adminAuthGroupAccessDao;

    /**
     * 添加用户组
     * @param group
     * @return
     */
    public Result add(AdminAuthGroup group) {
        adminAuthGroupDao.save(group);
        return Result.wrapResult(AdminAuthGroupLang.ADD_SUCCESS);
    }

    /**
     * 修改用户组权限
     * @param group
     * @param rules
     * @return
     */
    @Transactional
    public Result modifyRules(AdminAuthGroup group, List<AdminAuthRule> rules) {
        List<Long> ruleIds = Lists.transform(rules, new Function<AdminAuthRule, Long>() {
            @Override
            public Long apply(AdminAuthRule adminAuthRule) {
                return adminAuthRule.getId();
            }
        });
        group.setRules(Joiner.on(",").join(ruleIds));
        adminAuthGroupDao.save(group);
        return Result.wrapResult(AdminAuthGroupLang.MODIFY_RULES_SUCCESS);
    }

    /**
     * 删除用户组
     * @param group
     * @return
     */
    @Transactional
    public Result delete(AdminAuthGroup group) {
        //  删除用户组
        adminAuthGroupDao.delete(group.getId());
        //  清理用户组下的用户
        adminAuthGroupAccessDao.deleteByGroupId(group.getId());
        return Result.wrapResult(AdminAuthRuleLang.DROP_SUCCESS);
    }

    /**
     * 编辑用户组
     * @param group
     * @return
     */
    public Result edit(AdminAuthGroup group) {
        adminAuthGroupDao.save(group);
        return Result.wrapResult(AdminAuthGroupLang.MODIFY_SUCCESS);
    }

    /**
     * 获取成员所在的用户组列表
     * @param uid
     * @return
     */
    public Result<List<AdminAuthGroup>> getGroupsByUserId(Long uid) {
        List<AdminAuthGroupAccess> accesses = adminAuthGroupAccessDao.findByUid(uid);
        if (CollectionUtils.isEmpty(accesses)) {
            return Result.wrapResult(AdminAuthGroupAccessLang.USER_HAS_NO_GROUP);
        }
        List<Long> groupIds = Lists.transform(accesses, new Function<AdminAuthGroupAccess, Long>() {
            @Override
            public Long apply(AdminAuthGroupAccess adminAuthGroupAccess) {
                return adminAuthGroupAccess.getGroupId();
            }
        });
        return Result.wrapResult(adminAuthGroupDao.findByIdIn(groupIds));
    }

    /**
     * 用户组列表
     * @param param
     * @param pageParam
     * @return
     */
    public PagingResult<AdminAuthGroup> search(SearchParam param, PageParam pageParam) {
        Page<AdminAuthGroup> page = adminAuthGroupDao.findAll(new Specification<AdminAuthGroup>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                return null;
            }
        }, new PageRequest(pageParam.getPage(), pageParam.getLimit()));
        return PagingResult.wrapResult(page.getContent(), page.getTotalElements(), pageParam.getPage(), pageParam.getLimit());
    }
}
