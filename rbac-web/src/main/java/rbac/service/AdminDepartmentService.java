package rbac.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import rbac.dao.AdminDepartmentDao;
import rbac.dao.AdminDepartmentUserDao;
import rbac.dao.repository.AdminDepartment;
import rbac.dao.repository.AdminDepartmentUser;
import rbac.utils.Result;
import rbac.web.lang.AdminDepartmentLang;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by pandaking on 2017/5/24.
 */
@Component
public class AdminDepartmentService {

    @Autowired
    private AdminDepartmentDao adminDepartmentDao;
    @Autowired
    private AdminDepartmentUserDao adminDepartmentUserDao;

    /**
     * 添加部门（子级）及该部门中包含的成员
     *
     * 添加时，会将该部门下的用户信息先清理，再保存
     * @param department
     * @param pUuid
     * @param departmentUserList
     * @return
     */
    @Transactional
    public Result addDepartment(AdminDepartment department, String pUuid, List<AdminDepartmentUser> departmentUserList) {
        if (StringUtils.isNotBlank(pUuid)) {
            AdminDepartment parent = adminDepartmentDao.findByUuid(pUuid);
            if (parent == null) {
                return Result.wrapResult(AdminDepartmentLang.NOT_FOUND);
            }
            department.setPid(parent.getId());
        }
        adminDepartmentDao.save(department);
        for (AdminDepartmentUser item : departmentUserList) {
            item.setDepartmentId(department.getId());
            item.setDepartmentUuid(department.getUuid());
        }
        adminDepartmentUserDao.deleteByDepartmentId(department.getId());
        adminDepartmentUserDao.save(departmentUserList);
        return Result.wrapResult(AdminDepartmentLang.ADD_SUCCESS);
    }

    /**
     * 添加父级部门、部门中的成员
     * @param department
     * @param childUuid
     * @param departmentUserList
     * @return
     */
    @Transactional
    public Result addParentDepartment(AdminDepartment department, String childUuid, List<AdminDepartmentUser> departmentUserList) {
        AdminDepartment child = adminDepartmentDao.findByUuid(childUuid);
        if (child == null) {
            return Result.wrapResult(AdminDepartmentLang.NOT_FOUND);
        }
        addDepartment(department, null, departmentUserList);
        child.setPid(department.getId());
        adminDepartmentDao.save(child);
        return Result.wrapResult(AdminDepartmentLang.ADD_SUCCESS);
    }

    /**
     * 编辑部门信息、更新部门的成员
     * @param department
     * @param departmentUserList
     * @return
     */
    @Transactional
    public Result save(AdminDepartment department, List<AdminDepartmentUser> departmentUserList) {
        AdminDepartment adminDepartment = adminDepartmentDao.findByUuid(department.getUuid());
        if (adminDepartment == null) {
            return Result.wrapResult(AdminDepartmentLang.NOT_FOUND);
        }
        department.setId(adminDepartment.getId());
        adminDepartmentDao.save(department);
        adminDepartmentUserDao.deleteByDepartmentId(department.getId());
        adminDepartmentUserDao.save(departmentUserList);
        return Result.wrapResult(AdminDepartmentLang.ADD_SUCCESS);
    }

    /**
     * 删除部门，和部门下的用户配置
     * 删除失败：
     *      1.如果该部门下包含子部门
     * @param department
     * @return
     */
    @Transactional
    public Result delete(AdminDepartment department) {
        Integer childCount = adminDepartmentDao.countByPid(department.getId());
        if (childCount > 0) {
            return Result.wrapResult(AdminDepartmentLang.DELETE_FAILED_HAS_CHILD);
        }
        adminDepartmentDao.delete(department.getId());
        adminDepartmentUserDao.deleteByDepartmentId(department.getId());
        return Result.wrapResult(AdminDepartmentLang.DELETE_SUCCESS);
    }

    /**
     * 获取用户管理的成员列表（成员的uuid)
     * @param uuid
     * @return
     */
    public Set<String> getManagedUserUuidByUserUuid(String uuid) {
        List<AdminDepartmentUser> departmentUserList = adminDepartmentUserDao.findDepartmentIdByUserUuid(uuid);
        Set<Long> childDepartmentIds = new HashSet<>();
        for (AdminDepartmentUser departmentUser : departmentUserList) {
            childDepartmentIds.addAll(getDepartmentIdByPid(departmentUser.getDepartmentId()));
        }
        List<AdminDepartmentUser> list = adminDepartmentUserDao.findUserUuidByDepartmentIdIn(childDepartmentIds);
        Set<String> userUuids = new HashSet<>();
        for (AdminDepartmentUser item : list) {
            userUuids.add(item.getUserUuid());
        }
        return userUuids;
    }

    /**
     * 获取用户管理的成员列表（成员的id)
     * @param id
     * @return
     */
    public Set<Long> getManagedUidByUserId(Long id) {
        List<AdminDepartmentUser> departmentUserList = adminDepartmentUserDao.findDepartmentIdByUid(id);
        Set<Long> childDepartmentIds = new HashSet<>();
        for (AdminDepartmentUser departmentUser : departmentUserList) {
            childDepartmentIds.addAll(getDepartmentIdByPid(departmentUser.getDepartmentId()));
        }
        List<AdminDepartmentUser> list = adminDepartmentUserDao.findUserUuidByDepartmentIdIn(childDepartmentIds);
        Set<Long> userUuids = new HashSet<>();
        for (AdminDepartmentUser item : list) {
            userUuids.add(item.getUid());
        }
        return userUuids;
    }

    /**
     * 获取该部门下管理的所有子(孙）部门
     * @param departmentId
     * @return
     */
    private Set<Long> getDepartmentIdByPid(Long departmentId) {
        List<AdminDepartment> departments = adminDepartmentDao.findByPid(departmentId);
        if (CollectionUtils.isEmpty(departments)) {
            return Collections.EMPTY_SET;
        }
        Set<Long> departmentIds = new HashSet<>();
        for (AdminDepartment item : departments) {
            departmentIds.add(item.getId());
            departmentIds.addAll(getDepartmentIdByPid(item.getId()));
        }
        return departmentIds;
    }
}
