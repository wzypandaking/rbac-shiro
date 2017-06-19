package rbac.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rbac.dao.AdminAuthGroupAccessDao;
import rbac.dao.AdminAuthGroupDao;
import rbac.dao.repository.AdminAuthGroup;
import rbac.dao.repository.AdminAuthGroupAccess;
import rbac.dao.repository.AdminUsers;
import rbac.utils.AdministratorUtil;
import rbac.utils.Result;
import rbac.web.lang.AdminAuthGroupAccessLang;
import rbac.web.lang.AdminAuthGroupLang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/18
 * @note: 请补充说明
 * @history:
 */
@Component
public class AdminAuthGroupAccessService {

    @Autowired
    private AdminAuthGroupAccessDao adminAuthGroupAccessDao;
    @Autowired
    private AdminAuthGroupDao adminAuthGroupDao;

    /**
     * 添加用户时，自动给用户分配用户组
     * @param user
     * @return
     */
    @Transactional
    public Result addUsers2Group(AdminUsers user) {
        if (AdministratorUtil.isSuper()) {
            return addUsers2Group(user, 2L);
        } else {
            return addUsers2Group(user, 3L);
        }
    }

    private Result addUsers2Group(AdminUsers user, Long groupId) {
        AdminAuthGroup group = adminAuthGroupDao.findById(groupId);
        if (group == null) {
            return Result.wrapResult(AdminAuthGroupLang.GROUP_NOT_EXISTS);
        }
        adminAuthGroupAccessDao.save(buildByGroupAndUser(group, user));
        return Result.wrapResult(AdminAuthGroupAccessLang.USER_UPDATE_SUCCESS);
    }

    private AdminAuthGroupAccess buildByGroupAndUser(AdminAuthGroup group, AdminUsers users) {
        AdminAuthGroupAccess data = new AdminAuthGroupAccess();
        data.setGroupId(group.getId());
        data.setUid(users.getId());
        return data;
    }

    /**
     *  给用户组分配成员,会将原来的用户进行清理，然后设置
     * @param group
     * @param list
     * @return
     */
    @Transactional
    public Result addUsers2Group(AdminAuthGroup group, Collection<AdminUsers> list) {
        List<AdminAuthGroupAccess> dataList = new ArrayList<>();
        for (AdminUsers item : list) {
            dataList.add(buildByGroupAndUser(group, item));
        }
        adminAuthGroupAccessDao.deleteByGroupId(group.getId());
        adminAuthGroupAccessDao.save(dataList);
        return Result.wrapResult(AdminAuthGroupAccessLang.USER_UPDATE_SUCCESS);
    }

    /**
     * 获得用户组所分配的成员ID
     * @param group
     * @return
     */
    public List<Long> getUserIdsByGroup(AdminAuthGroup group) {
        List<AdminAuthGroupAccess> list = adminAuthGroupAccessDao.findByGroupId(group.getId());
        return Lists.transform(list, new Function<AdminAuthGroupAccess, Long>() {
            @Override
            public Long apply(AdminAuthGroupAccess adminAuthGroupAccess) {
                return adminAuthGroupAccess.getUid();
            }
        });
    }
}
