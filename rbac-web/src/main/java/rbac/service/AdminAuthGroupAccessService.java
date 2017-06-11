package rbac.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import rbac.dao.AdminAuthGroupAccessDao;
import rbac.dao.repository.AdminAuthGroup;
import rbac.dao.repository.AdminAuthGroupAccess;
import rbac.dao.repository.AdminUsers;
import rbac.utils.Result;
import rbac.web.lang.AdminAuthGroupAccessLang;
import rbac.web.lang.AdminUsersLang;

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

    /**
     *  给用户组分配成员
     * @param group
     * @param list
     * @return
     */
    @Transactional
    public Result addUsers2Group(AdminAuthGroup group, Collection<AdminUsers> list) {
        List<AdminAuthGroupAccess> dataList = new ArrayList<>();
        for (AdminUsers item : list) {
            AdminAuthGroupAccess data = new AdminAuthGroupAccess();
            data.setGroupId(group.getId());
            data.setUid(item.getId());
            dataList.add(data);
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
