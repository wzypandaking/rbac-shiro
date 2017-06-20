package rbac.service;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import rbac.dao.AdminAdminNavDao;
import rbac.dao.repository.AdminAdminNav;
import rbac.dao.repository.AdminUsers;
import rbac.utils.AdministratorUtil;
import rbac.utils.Result;
import rbac.web.lang.AdminAdminNavLang;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by pandaking on 2017/5/24.
 */
@Component
public class AdminAdminNavService {

    @Autowired
    private AdminAdminNavDao adminAdminNavDao;
    @Autowired
    private AdminUsersService adminUsersService;
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
        Iterable<AdminAdminNav> iterable = getMenu(1L);
        if (AdministratorUtil.isSuper()) {
            return Lists.newArrayList(iterable);
        }
        Set<String> rules = AdministratorUtil.getRules();
        return filterMenu(iterable, rules);
    }

    /**
     * 过滤掉没有权限的菜单
     * @param iterable
     * @param rules
     * @return
     */
    private List<AdminAdminNav> filterMenu(Iterable<AdminAdminNav> iterable, Set<String> rules) {

        Iterator<AdminAdminNav> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            AdminAdminNav adminAdminNav = iterator.next();
            if(!rules.contains(adminAdminNav.getRule())) {
                iterator.remove();
            }
        }
        return Lists.newArrayList(iterable);
    }

    /**
     * 获取项目菜单
     * @param creator
     * @return
     */
    private Iterable<AdminAdminNav> getMenu(Long creator) {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "orderNumber"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sort = new Sort(orderList);
        return adminAdminNavDao.findAll(new Specification<AdminAdminNav>() {
            @Override
            public Predicate toPredicate(Root<AdminAdminNav> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("creator"), creator);
            }
        }, sort);
    }

    /**
     * 获取远程菜单
     * @param user
     * @return
     */
    public List<AdminAdminNav> getClientMenu(AdminUsers user) {
        Iterable<AdminAdminNav> iterable = getMenu(user.getId());
        Set<String> rules = adminUsersService.getRulesByUser(user).getData();
        return filterMenu(iterable, rules);
    }


}
