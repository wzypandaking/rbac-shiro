package rbac.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import rbac.dao.AdminAdminNavDao;
import rbac.dao.AdminAuthRuleDao;
import rbac.dao.repository.AdminAdminNav;
import rbac.dao.repository.AdminAuthRule;
import rbac.dao.repository.AdminUsers;
import rbac.utils.AdministratorUtil;
import rbac.utils.Result;
import rbac.web.lang.AdminAdminNavLang;
import rbac.web.lang.SystemLang;

import java.util.*;

/**
 * Created by pandaking on 2017/5/24.
 */
@Component
public class AdminAdminNavService {

    @Autowired
    private AdminAdminNavDao adminAdminNavDao;
    @Autowired
    private AdminAuthRuleDao adminAuthRuleDao;

    /**
     * 添加菜单（子菜单）
     * 如果pUuid不为空，则天加子菜单
     * @param nav
     * @param pUuid
     * @return
     */
    public Result addNav(AdminAdminNav nav, String pUuid) {
        if (StringUtils.isNotBlank(pUuid)) {
            AdminAdminNav parent = adminAdminNavDao.findByUuid(pUuid);
            if (parent == null) {
                return Result.wrapResult(AdminAdminNavLang.NOT_FOUND);
            }
            nav.setPid(parent.getId());
        }
        adminAdminNavDao.save(nav);
        return Result.wrapResult(AdminAdminNavLang.ADD_SUCCESS);
    }

    /**
     * 修改菜单
     * @param nav
     * @return
     */
    public Result save(AdminAdminNav nav) {
        AdminAdminNav adminAdminNav = adminAdminNavDao.findByUuid(nav.getUuid());
        if (adminAdminNav == null) {
            return Result.wrapResult(AdminAdminNavLang.NOT_FOUND);
        }
        nav.setId(adminAdminNav.getId());
        adminAdminNavDao.save(nav);
        return Result.wrapResult(AdminAdminNavLang.ADD_SUCCESS);
    }

    /**
     * 删除菜单，如果菜单包含子菜单，删除不成功
     * @param nav
     * @return
     */
    public Result delete(AdminAdminNav nav) {
        if (adminAdminNavDao.countByPid(nav.getId()) > 0) {
            return Result.wrapResult(AdminAdminNavLang.DELETE_FAILED_HAS_CHILD);
        }
        adminAdminNavDao.delete(nav.getId());
        return Result.wrapResult(AdminAdminNavLang.DELETE_SUCCESS);
    }

    /**
     * 获取菜单列表
     * @return
     */
    public List<AdminAdminNav> getMenu() {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "orderNumber"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sort = new Sort(orderList);
        Iterable<AdminAdminNav> iterable = adminAdminNavDao.findAll(sort);
        if (AdministratorUtil.isSuper()) {
            return Lists.newArrayList(iterable);
        }

        Set<String> rules = AdministratorUtil.getRules();
        Iterator<AdminAdminNav> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            AdminAdminNav adminAdminNav = iterator.next();
            if(!rules.contains(adminAdminNav.getRule())) {
                iterator.remove();
            }
        }
        return Lists.newArrayList(iterable);
    }
}
