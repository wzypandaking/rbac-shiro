package rbac.service;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rbac.dao.AdminAuthRuleDao;
import rbac.dao.repository.AdminAuthGroup;
import rbac.dao.repository.AdminAuthRule;
import rbac.utils.Result;
import rbac.web.lang.AdminAuthGroupLang;
import rbac.web.lang.AdminAuthRuleLang;

import java.util.Collections;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/17
 * @note: 请补充说明
 * @history:
 */
@Component
public class AdminAuthRuleService {

    @Autowired
    private AdminAuthRuleDao adminAuthRuleDao;

    /**
     * 删除权限
     * @param rule
     * @return
     */
    public Result delete(AdminAuthRule rule) {
        Integer childCount = adminAuthRuleDao.countByPid(rule.getId());
        if (childCount > 0) {
            return Result.wrapResult(AdminAuthRuleLang.DELETE_FAILED_HAS_CHILD);
        }
        adminAuthRuleDao.delete(rule.getId());
        return Result.wrapResult(AdminAuthRuleLang.DROP_SUCCESS);
    }

    /**
     * 保存权限值
     * @param rule
     * @return
     */
    public Result save(AdminAuthRule rule) {
        adminAuthRuleDao.save(rule);
        return Result.wrapResult(AdminAuthRuleLang.SAVE_SUCCESS);
    }

    /**
     * 添加权限(子权限）
     * @param rule
     * @param pUuid
     * @return
     */
    public Result add(AdminAuthRule rule, String pUuid) {
        if (StringUtils.isNoneBlank(pUuid)) {
            //  验证父级信息是否存在
            AdminAuthRule parent = adminAuthRuleDao.findByUuid(pUuid);
            if (parent == null) {
                return Result.wrapResult(AdminAuthRuleLang.PARENT_NOT_FOUND);
            }
            rule.setPid(parent.getId());
            rule.setName(String.format("%s/%s", parent.getName(), rule.getName()));
        }
        adminAuthRuleDao.save(rule);
        return Result.wrapResult(AdminAuthRuleLang.SAVE_SUCCESS);
    }

    /**
     * 获取用户组分配的权限
     * @param group
     * @return
     */
    public Result<List<AdminAuthRule>> getRules(AdminAuthGroup group) {
        if (group == null) {
            return Result.wrapResult(AdminAuthGroupLang.GROUP_NOT_EXISTS);
        }
        if (StringUtils.isEmpty(group.getRules())) {
            return Result.wrapResult(AdminAuthGroupLang.HAS_NO_RULES);
        }
        List<Long> ruleIds = Lists.transform(Splitter.on(",").splitToList(group.getRules()), new Function<String, Long>() {
            @Override
            public Long apply(String f) {
                return Long.valueOf(f);
            }
        });
        return Result.wrapResult(adminAuthRuleDao.findByIdIn(ruleIds));
    }
}
