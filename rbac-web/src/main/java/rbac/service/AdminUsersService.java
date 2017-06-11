package rbac.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import rbac.dao.AdminUsersDao;
import rbac.dao.repository.AdminAuthGroup;
import rbac.dao.repository.AdminAuthRule;
import rbac.dao.repository.AdminUsers;
import rbac.utils.*;
import rbac.web.lang.AdminUsersLang;
import rbac.web.lang.LoginLang;
import rbac.web.lang.SystemLang;
import rbac.web.param.PageParam;
import rbac.web.param.UserListParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
@Component
public class AdminUsersService {

    private Logger logger = LoggerFactory.getLogger(AdminUsersService.class);

    @Autowired
    private AdminUsersDao adminUsersDao;
    @Autowired
    private AdminAuthGroupService adminAuthGroupService;
    @Autowired
    private AdminAuthRuleService adminAuthRuleService;

    /**
     * 构建密码加密规则
     * @param source
     * @param salt
     * @return
     */
    private String buildPassword(String source, String salt) {
        return Md5Util.md5(Md5Util.md5(source) + salt);
    }

    /**
     * 检查用户账号密码
     * @param username
     * @param password
     * @return
     */
    public AdminUsers checkUserAndPassword(String username, String password) {
        AdminUsers adminUsers = adminUsersDao.findByUsername(username);
        if (adminUsers == null) {
            logger.error("没有找到用户 username:{}, password{}", username, password);
            return null;
        }
        if (!adminUsers.getPassword().equals(buildPassword(password, adminUsers.getSalt()))) {
            return null;
        }
        return adminUsers;
    }

    /**
     * 用户登录信息记录
     * @param user
     */
    public void userLogin(AdminUsers user) {
        user.setLastLoginTime(new Date());
        user.setLastLoginIp(IPUtil.ip());
        adminUsersDao.save(user);
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    public Long addUser(AdminUsers user) {
        String salt = RandomUtil.getCode(4, 3);
        String password = buildPassword(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(password);
        user.setRegisterTime(new Date());
        adminUsersDao.save(user);
        return user.getId();
    }

    /**
     * 用户搜索
     * @param param
     * @param page
     * @return
     */
    public PagingResult<AdminUsers> search(UserListParam param, PageParam page) {
        Page<AdminUsers> result = adminUsersDao.findAll(new Specification<AdminUsers>() {
            @Override
            public Predicate toPredicate(Root<AdminUsers> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return null;
            }
        }, new PageRequest(page.getPage(), page.getLimit()));
        return PagingResult.wrapResult(result.getContent(), result.getTotalElements(), page.getPage() + 1, page.getLimit());
    }

    /**
     * 获取用户的权限列表
     * @param user
     * @return
     */
    public Result<Set<String>> getRulesByUser(AdminUsers user) {
        List<AdminAuthGroup> list = new ArrayList<>();
        {
            Result checkResult = checkUser(user);
            if (!checkResult.isSuccess()) {
                return Result.wrapErrorResult(checkResult.getMessage());
            }
            Result<List<AdminAuthGroup>> result = adminAuthGroupService.getGroupsByUserId(user.getId());
            if (!result.isSuccess()) {
                return Result.wrapErrorResult(result.getMessage());
            }
            list.addAll(result.getData());
        }
        Set<String> permissions = new HashSet<>();
        for (AdminAuthGroup group : list) {
            Result<List<AdminAuthRule>> result = adminAuthRuleService.getRules(group);
            if (!result.isSuccess()) continue;
            for (AdminAuthRule rule : result.getData()) {
                permissions.add(rule.getName());
            }
        }
        return Result.wrapResult(permissions);
    }

    /**
     *
     * @param adminUsers
     * @return
     */
    public Result checkUser(AdminUsers adminUsers) {
        if (adminUsers == null) {
            return Result.wrapResult(LoginLang.FAILED);
        }
        if (adminUsers.getStatus() != 1) {
            return Result.wrapResult(AdminUsersLang.LOGIN_FORBID);
        }
        return Result.wrapResult(SystemLang.SUCCESS);
    }

}
