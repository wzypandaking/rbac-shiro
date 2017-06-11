package rbac.web;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rbac.RbacPermissions;
import rbac.dao.AdminAuthRuleDao;
import rbac.dao.repository.AdminAuthRule;
import rbac.service.AdminAuthRuleService;
import rbac.utils.BeanUtil;
import rbac.utils.Result;
import rbac.web.lang.AdminAuthRuleLang;
import rbac.web.param.AdminAuthRuleAddParam;
import rbac.web.param.AdminAuthRuleEditParam;
import rbac.web.shiro.RequiresPermissions;
import rbac.web.vo.AdminAuthRuleItemVO;
import rbac.web.vo.AdminAuthRuleVO;

import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/17
 * @note: 请补充说明
 * @history:
 */
@RestController
@RequestMapping("rules")
public class AuthRuleController {

    @Autowired
    private AdminAuthRuleDao adminAuthRuleDao;
    @Autowired
    private AdminAuthRuleService adminAuthRuleService;

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_RULE
    })
    @RequestMapping("list")
    @ResponseBody
    public Result<List<AdminAuthRuleVO>> list() {
        List<AdminAuthRuleVO> list = BeanUtil.copy(adminAuthRuleDao.findAll(), new TypeReference<List<AdminAuthRuleVO>>(){}.getType());
        return Result.wrapResult(list);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_RULE_DELETE
    })
    @RequestMapping("delete")
    public Result delete(String uuid) {
        AdminAuthRule rule = adminAuthRuleDao.findByUuid(uuid);
        if (rule == null) {
            return Result.wrapResult(AdminAuthRuleLang.NOT_FOUND);
        }
        return adminAuthRuleService.delete(rule);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_RULE_EDIT
    })
    @RequestMapping("item")
    @ResponseBody
    public Result<AdminAuthRuleItemVO> item(String uuid) {
        AdminAuthRule rule = adminAuthRuleDao.findByUuid(uuid);
        if (rule == null) {
            return Result.wrapResult(AdminAuthRuleLang.NOT_FOUND);
        }
        AdminAuthRuleItemVO item = BeanUtil.copy(rule, AdminAuthRuleItemVO.class);
        return Result.wrapResult(item);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_RULE_EDIT
    })
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Result edit(AdminAuthRuleEditParam param) {
        AdminAuthRule adminAuthRule = adminAuthRuleDao.findByUuid(param.getUuid());
        if (adminAuthRule == null) {
            return Result.wrapResult(AdminAuthRuleLang.NOT_FOUND);
        }
        AdminAuthRule rule = BeanUtil.copy(param, AdminAuthRule.class);
        rule.setId(adminAuthRule.getId());
        return adminAuthRuleService.save(rule);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_RULE_ADD
    })
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(AdminAuthRuleAddParam param) {
        AdminAuthRule adminAuthRule = BeanUtil.copy(param, AdminAuthRule.class);
        return adminAuthRuleService.add(adminAuthRule, param.getPUuid());
    }
}
