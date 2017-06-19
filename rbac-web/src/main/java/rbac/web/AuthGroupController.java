package rbac.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rbac.RbacPermissions;
import rbac.dao.AdminAuthGroupDao;
import rbac.dao.AdminAuthRuleDao;
import rbac.dao.AdminUsersDao;
import rbac.dao.repository.AdminAuthGroup;
import rbac.dao.repository.AdminAuthRule;
import rbac.dao.repository.AdminUsers;
import rbac.service.AdminAuthGroupAccessService;
import rbac.service.AdminAuthGroupService;
import rbac.utils.BeanUtil;
import rbac.utils.PagingResult;
import rbac.utils.Result;
import rbac.web.lang.AdminAuthGroupLang;
import rbac.web.lang.AdminAuthRuleLang;
import rbac.web.lang.AdminUsersLang;
import rbac.web.param.*;
import rbac.web.shiro.RequiresPermissions;
import rbac.web.vo.AdminAuthGroupVO;
import rbac.web.vo.ChooseRuleVO;
import rbac.web.vo.ChooseUserVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/17
 * @note: 请补充说明
 * @history:
 */
@RestController
@RequestMapping("auth/group")
public class AuthGroupController {

    @Autowired
    private AdminAuthGroupService adminAuthGroupService;
    @Autowired
    private AdminAuthGroupDao adminAuthGroupDao;
    @Autowired
    private AdminUsersDao adminUsersDao;
    @Autowired
    private AdminAuthGroupAccessService adminAuthGroupAccessService;
    @Autowired
    private AdminAuthRuleDao adminAuthRuleDao;


    @RequiresPermissions(value = {
            RbacPermissions.RBAC_AUTHORITY_GROUP
    })
    @RequestMapping("list")
    @ResponseBody
    public PagingResult<AdminAuthGroupVO> list(SearchParam param, PageParam pageParam) {
        PagingResult<AdminAuthGroup> result = adminAuthGroupService.search(param, pageParam);
        List<AdminAuthGroupVO> groups = BeanUtil.copy(result.getList(), new TypeReference<List<AdminAuthGroupVO>>() {
        }.getType());
        return PagingResult.wrapResult(groups, result.getTotal(), result.getPage(), result.getSize());
    }

    @RequiresPermissions(value = {
            RbacPermissions.RBAC_AUTHORITY_GROUP_EDIT
    })
    @RequestMapping("item")
    @ResponseBody
    public Result<AdminAuthGroupVO> item(String uuid) {
        AdminAuthGroupVO adminAuthGroupVO = BeanUtil.copy(adminAuthGroupDao.findByUuid(uuid), AdminAuthGroupVO.class);
        return Result.wrapResult(adminAuthGroupVO);
    }

    @RequiresPermissions(value = {
            RbacPermissions.RBAC_AUTHORITY_GROUP_EDIT
    })
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Result add(AdminAuthGroupEditParam param) {
        AdminAuthGroup adminAuthGroup = adminAuthGroupDao.findByUuid(param.getUuid());
        if (adminAuthGroup == null) {
            return Result.wrapResult(AdminAuthGroupLang.GROUP_NOT_EXISTS);
        }
        adminAuthGroup.setTitle(param.getTitle());
        return adminAuthGroupService.edit(adminAuthGroup);
    }

    @RequiresPermissions(value = {
            RbacPermissions.RBAC_AUTHORITY_GROUP_ADD
    })
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(String title) {
        AdminAuthGroup adminAuthGroup = adminAuthGroupDao.findByTitle(title);
        if (adminAuthGroup != null) {
            return Result.wrapResult(AdminAuthGroupLang.GROUP_EXISTS);
        }
        AdminAuthGroup group = new AdminAuthGroup();
        group.setTitle(title);
        return adminAuthGroupService.add(group);
    }

    @RequiresPermissions(value = {
            RbacPermissions.RBAC_AUTHORITY_GROUP_RULES
    })
    @RequestMapping(value = "rules", method = RequestMethod.POST)
    @ResponseBody
    public Result addRules(AdminAuthGroupRuleParam param) {
        AdminAuthGroup group = adminAuthGroupDao.findByUuid(param.getUuid());
        if (group == null) {
            return Result.wrapResult(AdminAuthGroupLang.GROUP_NOT_EXISTS);
        }
        List<AdminAuthRule> rules = new ArrayList<>();
        if (StringUtils.isNotEmpty(param.getRules())) {
            rules = adminAuthRuleDao.findByUuidIn(Splitter.on(",").splitToList(param.getRules()));
            if (CollectionUtils.isEmpty(rules)) {
                return Result.wrapResult(AdminAuthRuleLang.NOT_FOUND);
            }
        }
        return adminAuthGroupService.modifyRules(group, rules);
    }

    @RequiresPermissions(value = {
            RbacPermissions.RBAC_AUTHORITY_GROUP_RULES
    })
    @RequestMapping(value = "choosedRules")
    @ResponseBody
    public Result<List<ChooseRuleVO>> choosedRules(String uuid) {
        AdminAuthGroup group = adminAuthGroupDao.findByUuid(uuid);
        if (group == null) {
            return Result.wrapResult(AdminAuthGroupLang.GROUP_NOT_EXISTS);
        }

        //  获取权限列表
        List<AdminAuthRule> rules = Lists.newArrayList(adminAuthRuleDao.findAll());
        if (CollectionUtils.isEmpty(rules)) {
            return Result.wrapResult(AdminAuthRuleLang.NOT_FOUND);
        }

        List<Long> chooseRuledIds = new ArrayList<>();
        if (StringUtils.isNotBlank(group.getRules())) {
            chooseRuledIds = Lists.transform(Splitter.on(",").splitToList(group.getRules()), new Function<String, Long>() {
                @Override
                public Long apply(String s) {
                    return Long.valueOf(s);
                }
            });
        }
        List<ChooseRuleVO> chooseRuleList = new ArrayList<>();
        for (AdminAuthRule rule : rules) {
            ChooseRuleVO chooseRule = BeanUtil.copy(rule, ChooseRuleVO.class);
            chooseRule.setChoose(chooseRuledIds.contains(rule.getId()));
            chooseRuleList.add(chooseRule);
        }
        return Result.wrapResult(chooseRuleList);
    }

    @RequiresPermissions(value = {
            RbacPermissions.RBAC_AUTHORITY_GROUP_DELETE
    })
    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String uuid) {
        AdminAuthGroup group = adminAuthGroupDao.findByUuid(uuid);
        if (group == null) {
            return Result.wrapResult(AdminAuthGroupLang.GROUP_NOT_EXISTS);
        } else if (group.getType() == 1L) {
            return Result.wrapResult(AdminAuthGroupLang.GROUP_NOT_EXISTS);
        }
        return adminAuthGroupService.delete(group);
    }

    @RequiresPermissions(value = {
            RbacPermissions.RBAC_AUTHORITY_GROUP_USERS
    })
    @RequestMapping("users")
    @ResponseBody
    public Result<List<ChooseUserVO>> users(String uuid) {
        /**
         * 检查用户组
         */
        AdminAuthGroup group = adminAuthGroupDao.findByUuid(uuid);
        if (group == null) {
            return Result.wrapResult(AdminAuthGroupLang.GROUP_NOT_EXISTS);
        }
        //  用户组中分配的权限
        List<Long> userIds = adminAuthGroupAccessService.getUserIdsByGroup(group);
        //  转化为结果
        List<AdminUsers> userList = Lists.newArrayList(adminUsersDao.findAll());
        List<ChooseUserVO> resultList = new ArrayList<>();
        for (AdminUsers item : userList) {
            ChooseUserVO vo = BeanUtil.copy(item, ChooseUserVO.class);
            vo.setChoose(userIds.contains(item.getId()));
            resultList.add(vo);
        }
        return Result.wrapResult(resultList);
    }

    @RequiresPermissions(value = {
            RbacPermissions.RBAC_AUTHORITY_GROUP_USERS
    })
    @RequestMapping("user/add")
    @ResponseBody
    public Result users(AdminGroupUserAddParam param) {
        AdminAuthGroup adminAuthGroup = adminAuthGroupDao.findByUuid(param.getGroupUuid());
        if (adminAuthGroup == null) {
            return Result.wrapResult(AdminAuthGroupLang.GROUP_NOT_EXISTS);
        }
        List<AdminUsers> userList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(param.getUserUuid())) {
            userList = adminUsersDao.findByUuidIn(param.getUserUuid());
            if (CollectionUtils.isEmpty(userList)) {
                return Result.wrapResult(AdminUsersLang.NOT_FOUND);
            }
        }
        return adminAuthGroupAccessService.addUsers2Group(adminAuthGroup, userList);
    }
}
