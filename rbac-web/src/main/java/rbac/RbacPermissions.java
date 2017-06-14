package rbac;

/**
 * Created by pandaking on 2017/5/31.
 */
public enum RbacPermissions {
    RBAC("rbac", "权限系统"),
	RBAC_SYSTEM("rbac/system", "系统控制"),
	RBAC_SYSTEM_MENU("rbac/system/menu", "菜单管理"),
	RBAC_SYSTEM_MENU_ADD("rbac/system/menu/add", "添加菜单"),
	RBAC_SYSTEM_MENU_EDIT("rbac/system/menu/edit", "修改菜单"),
	RBAC_SYSTEM_MENU_DELETE("rbac/system/menu/delete", "删除菜单"),
	RBAC_VC("rbac/vc", "版本控制"),
	RBAC_VC_VERSION("rbac/vc/version", "版本管理"),
	RBAC_VC_LICENSE("rbac/vc/license", "序列号管理"),
	RBAC_VC_VERSION_ADD("rbac/vc/version/add", "添加版本"),
	RBAC_VC_VERSION_LICENSE("rbac/vc/version/license", "查看序列号"),
	RBAC_VC_VERSION_LICENSE_ADD("rbac/vc/version/license/add", "添加序列号"),
	RBAC_VC_LICENSE_VIEW("rbac/vc/license/view", "查看序列号"),
	RBAC_AUTHORITY("rbac/authority", "权限控制"),
	RBAC_AUTHORITY_USER("rbac/authority/user", "账号管理"),
	RBAC_AUTHORITY_USER_ADD("rbac/authority/user/add", "添加账号"),
	RBAC_AUTHORITY_USER_LOGIN("rbac/authority/user/login", "禁止账号登陆"),
	RBAC_AUTHORITY_GROUP("rbac/authority/group", "用户组管理"),
	RBAC_AUTHORITY_GROUP_ADD("rbac/authority/group/add", "添加用户组"),
	RBAC_AUTHORITY_GROUP_EDIT("rbac/authority/group/edit", "修改用户组"),
	RBAC_AUTHORITY_GROUP_DELETE("rbac/authority/group/delete", "删除用户组"),
	RBAC_AUTHORITY_GROUP_RULES("rbac/authority/group/rules", "分配权限"),
	RBAC_AUTHORITY_GROUP_USERS("rbac/authority/group/users", "分配账号"),
	RBAC_AUTHORITY_RULE("rbac/authority/rule", "权限管理"),
	RBAC_AUTHORITY_RULE_ADD("rbac/authority/rule/add", "添加权限"),
	RBAC_AUTHORITY_RULE_EDIT("rbac/authority/rule/edit", "修改权限"),
	RBAC_AUTHORITY_RULE_DELETE("rbac/authority/rule/delete", "删除权限"),
	RBAC_AUTHORITY_DEPARTMENT("rbac/authority/department", "部门管理"),
	RBAC_AUTHORITY_DEPARTMENT_ADD("rbac/authority/department/add", "添加部门"),
	RBAC_AUTHORITY_DEPARTMENT_EDIT("rbac/authority/department/edit", "修改部门"),
	RBAC_AUTHORITY_DEPARTMENT_DELETE("rbac/authority/department/delete", "删除部门"),
	RBAC_AUTHORITY_DEPARTMENT_USERS("rbac/authority/department/users", "分配用户"),
	RBAC_VC_LICENSE_STATUS("rbac/vc/license/status", "切换序列号状态"),
	RBAC_PROFILE("rbac/profile", "个人中心"),
	RBAC_PROFILE_INFO("rbac/profile/info", "个人信息"),
	RBAC_PROFILE_CHPASWD("rbac/profile/chpaswd", "修改密码"),
	RBAC_AUTHORITY_USER_CHPASSWD("rbac/authority/user/chpasswd", "修改密码"),

    ;
    private String name;
    private String title;

    RbacPermissions(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }
}