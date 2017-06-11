package rbac.web;

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
import rbac.dao.AdminDepartmentDao;
import rbac.dao.AdminDepartmentUserDao;
import rbac.dao.AdminUsersDao;
import rbac.dao.repository.AdminDepartment;
import rbac.dao.repository.AdminDepartmentUser;
import rbac.dao.repository.AdminUsers;
import rbac.service.AdminDepartmentService;
import rbac.utils.BeanUtil;
import rbac.utils.Result;
import rbac.web.lang.AdminDepartmentLang;
import rbac.web.lang.SystemLang;
import rbac.web.param.DepartmentAddParam;
import rbac.web.param.DepartmentEditParam;
import rbac.web.shiro.RequiresPermissions;
import rbac.web.vo.ChooseUserVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pandaking on 2017/5/24.
 */
@RestController
@RequestMapping("department")
public class DepartmentController {

    @Autowired
    private AdminDepartmentDao adminDepartmentDao;
    @Autowired
    private AdminDepartmentService adminDepartmentService;
    @Autowired
    private AdminUsersDao adminUsersDao;
    @Autowired
    private AdminDepartmentUserDao adminDepartmentUserDao;

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_DEPARTMENT
    })
    @RequestMapping("list")
    @ResponseBody
    public Result list() {
        return Result.wrapResult(adminDepartmentDao.findAll());
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_DEPARTMENT_EDIT
    })
    @RequestMapping("item")
    @ResponseBody
    public Result item(String uuid) {
        return Result.wrapResult(adminDepartmentDao.findByUuid(uuid));
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_DEPARTMENT_DELETE
    })
    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String uuid) {
        AdminDepartment department = adminDepartmentDao.findByUuid(uuid);
        if (department == null) {
            return Result.wrapResult(AdminDepartmentLang.NOT_FOUND);
        }
        return adminDepartmentService.delete(department);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_DEPARTMENT_ADD
    })
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(DepartmentAddParam param) {
        AdminDepartment department = BeanUtil.copy(param, AdminDepartment.class);
        List<AdminDepartmentUser> departmentUserList = processDepartmentUsers(department, param.getUserUuid());
        return adminDepartmentService.addDepartment(department, param.getPUuid(), departmentUserList);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_DEPARTMENT_ADD
    })
    @RequestMapping(value = "addParent", method = RequestMethod.POST)
    @ResponseBody
    public Result addParent(DepartmentAddParam param) {
        if (StringUtils.isBlank(param.getPUuid())) {
            return Result.wrapResult(AdminDepartmentLang.PARAM_ERROR);
        }
        AdminDepartment department = BeanUtil.copy(param, AdminDepartment.class);
        List<AdminDepartmentUser> departmentUserList = processDepartmentUsers(department, param.getUserUuid());
        return adminDepartmentService.addParentDepartment(department, param.getPUuid(), departmentUserList);
    }

    private List<AdminDepartmentUser> processDepartmentUsers(AdminDepartment department, String userUuids) {
        if (StringUtils.isNotBlank(userUuids)) {
            List<AdminUsers> users = adminUsersDao.findByUuidIn(Splitter.on(",").splitToList(userUuids));
            if (!CollectionUtils.isEmpty(users)) {
                List<AdminDepartmentUser> departmentUserList = new ArrayList<>();
                for (AdminUsers item : users) {
                    AdminDepartmentUser departmentUser = new AdminDepartmentUser();
                    departmentUser.setUid(item.getId());
                    departmentUser.setUserUuid(item.getUuid());
                    departmentUser.setDepartmentId(department.getId());
                    departmentUser.setDepartmentUuid(department.getUuid());
                    departmentUserList.add(departmentUser);
                }
                return departmentUserList;
            }
        }
        return Collections.EMPTY_LIST;
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_DEPARTMENT_EDIT
    })
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Result edit(DepartmentEditParam param) {
        AdminDepartment department = BeanUtil.copy(param, AdminDepartment.class);
        List<AdminDepartmentUser> departmentUserList = processDepartmentUsers(department, param.getUserUuid());
        return adminDepartmentService.save(department, departmentUserList);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_DEPARTMENT_ADD,
            RbacPermissions.RBAC_AUTHORITY_DEPARTMENT_EDIT,
    })
    @RequestMapping("users")
    @ResponseBody
    public Result<List<ChooseUserVO>> users(String uuid) {
        List<AdminUsers> usersList = Lists.newArrayList(adminUsersDao.findAll());

        List<ChooseUserVO> list = new ArrayList<>(usersList.size());
        List<Long> chooseUserIds = new ArrayList<>();
        // 加载部门中的用户
        if (StringUtils.isNotEmpty(uuid) || StringUtils.isNotBlank(uuid)) {
            List<AdminDepartmentUser> departmentUserList = adminDepartmentUserDao.findUidByDepartmentUuid(uuid);
            if (!CollectionUtils.isEmpty(departmentUserList)) {
                chooseUserIds = Lists.transform(departmentUserList, new Function<AdminDepartmentUser, Long>() {
                    @Override
                    public Long apply(AdminDepartmentUser adminDepartmentUser) {
                        return adminDepartmentUser.getUid();
                    }
                });
            }
        }
        for (AdminUsers item : usersList) {
            ChooseUserVO user = BeanUtil.copy(item, ChooseUserVO.class);
            if (chooseUserIds.contains(item.getId())) {
                user.setChoose(true);
            }
            list.add(user);
        }
        return Result.wrapResult(list);
    }

}
