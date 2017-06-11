package rbac.web;

import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rbac.RbacPermissions;
import rbac.dao.AdminAdminNavDao;
import rbac.dao.AdminAuthRuleDao;
import rbac.dao.repository.AdminAdminNav;
import rbac.dao.repository.AdminAuthRule;
import rbac.service.AdminAdminNavService;
import rbac.utils.BeanUtil;
import rbac.utils.Result;
import rbac.web.lang.AdminAdminNavLang;
import rbac.web.lang.AdminAuthRuleLang;
import rbac.web.param.MenuAddParam;
import rbac.web.param.MenuEditParam;
import rbac.web.shiro.RequiresPermissions;
import rbac.web.vo.MenuItemVO;
import rbac.web.vo.MenuVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pandaking on 2017/5/24.
 */
@RestController
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private AdminAdminNavDao adminAdminNavDao;
    @Autowired
    private AdminAdminNavService adminAdminNavService;
    @Autowired
    private AdminAuthRuleDao adminAuthRuleDao;

    @RequiresPermissions({
            RbacPermissions.RBAC_SYSTEM_MENU
    })
    @RequestMapping("list")
    @ResponseBody
    public Result list() {
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.ASC, "orderNumber"));
        orderList.add(new Sort.Order(Sort.Direction.ASC, "id"));
        Sort sort = new Sort(orderList);
        return Result.wrapResult(adminAdminNavDao.findAll(sort));
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_SYSTEM_MENU_EDIT
    })
    @RequestMapping("item")
    @ResponseBody
    public Result<MenuItemVO> item(String uuid) {
        AdminAdminNav nav = adminAdminNavDao.findByUuid(uuid);
        if (nav == null) {
            return Result.wrapResult(AdminAdminNavLang.NOT_FOUND);
        }
        MenuItemVO item = BeanUtil.copy(nav, MenuItemVO.class);
        return Result.wrapResult(item);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_SYSTEM_MENU_DELETE
    })
    @RequestMapping("delete")
    @ResponseBody
    public Result delete(String uuid) {
        AdminAdminNav nav = adminAdminNavDao.findByUuid(uuid);
        if (nav == null) {
            return Result.wrapResult(AdminAdminNavLang.NOT_FOUND);
        }
        return adminAdminNavService.delete(nav);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_SYSTEM_MENU_ADD
    })
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(MenuAddParam param) {
        AdminAuthRule rule = adminAuthRuleDao.findByUuid(param.getRuleUuid());
        if (rule == null) {
            return Result.wrapResult(AdminAuthRuleLang.NOT_FOUND);
        }
        AdminAdminNav nav = BeanUtil.copy(param, AdminAdminNav.class);
        nav.setRule(rule.getName());
        return adminAdminNavService.addNav(nav, param.getPUuid());
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_SYSTEM_MENU_EDIT
    })
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ResponseBody
    public Result edit(MenuEditParam param) {
        AdminAuthRule rule = adminAuthRuleDao.findByUuid(param.getRuleUuid());
        if (rule == null) {
            return Result.wrapResult(AdminAuthRuleLang.NOT_FOUND);
        }
        AdminAdminNav nav = BeanUtil.copy(param, AdminAdminNav.class);
        nav.setRule(rule.getName());
        return adminAdminNavService.save(nav);
    }


    @RequestMapping("show")
    @ResponseBody
    public Result<List<MenuVO>> show() {
        List<AdminAdminNav> navList = adminAdminNavService.getMenu();
        List<MenuVO> menus = BeanUtil.copy(navList, new TypeReference<List<MenuVO>>(){}.getType());
        return Result.wrapResult(menus);
    }


}
