package rbac.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rbac.RbacPermissions;
import rbac.dao.AdminUsersDao;
import rbac.dao.repository.AdminUsers;
import rbac.service.AdminUsersService;
import rbac.utils.BeanUtil;
import rbac.utils.PagingResult;
import rbac.utils.Result;
import rbac.web.lang.AdminUsersLang;
import rbac.web.param.PageParam;
import rbac.web.param.UserAddParam;
import rbac.web.param.UserListParam;
import rbac.web.param.UserStatusParam;
import rbac.web.shiro.RequiresPermissions;
import rbac.web.vo.AdminUsersVO;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
@RestController
@RequestMapping("user")
public class UserController {

    private final Pattern emailPattern = Pattern.compile("^[a-z|A-Z|0-9]+@[a-z|A-Z|0-9]+.[a-z|A-Z]+$");
    private final Pattern mobilePattern = Pattern.compile("^1\\d{10}$");

    @Autowired
    private AdminUsersService adminUsersService;
    @Autowired
    private AdminUsersDao adminUsersDao;

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_USER
    })
    @RequestMapping("list")
    @ResponseBody
    public PagingResult<AdminUsersVO> list(UserListParam param, PageParam page) {
        PagingResult<AdminUsers> result = adminUsersService.search(param, page);
        List<AdminUsersVO> users = BeanUtil.copy(result.getList(), new TypeReference<List<AdminUsersVO>>(){}.getType());
        return PagingResult.wrapResult(users, result.getTotal(), result.getPage(), result.getSize());
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_USER_ADD
    })
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(UserAddParam param) {
        // todo 上传头像
        JSONObject result = checkUsername(param.getUsername());
        if(!result.getBoolean("valid")) {
            return Result.wrapErrorResult(result.getString("message"));
        }
        result = checkPasswordFormat(param.getPassword());
        if(!result.getBoolean("valid")) {
            return Result.wrapErrorResult(result.getString("message"));
        }
        result = checkEmail(param.getEmail());
        if(!result.getBoolean("valid")) {
            return Result.wrapErrorResult(result.getString("message"));
        }
        result = checkMobile(param.getPhone());
        if(!result.getBoolean("valid")) {
            return Result.wrapErrorResult(result.getString("message"));
        }
        AdminUsers user = BeanUtil.copy(param, AdminUsers.class);
        return Result.wrapResult(adminUsersService.addUser(user), AdminUsersLang.ADD_SUCCESS);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_USER_LOGIN
    })
    @RequestMapping("toggleStatus")
    @ResponseBody
    public Result toggleStatus(UserStatusParam param) {
        AdminUsers adminUsers = adminUsersDao.findByUuid(param.getUuid());
        if (adminUsers == null) {
            return Result.wrapResult(AdminUsersLang.NOT_FOUND);
        }
        adminUsers.setStatus(param.getStatus());
        adminUsersDao.save(adminUsers);
        return Result.wrapResult(AdminUsersLang.TOGGLE_STATUS_SUCCESS);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_USER_ADD,
    })
    @RequestMapping("checkUsername")
    @ResponseBody
    public JSONObject checkUsername(String username) {
        JSONObject object = new JSONObject();
        if (StringUtils.isBlank(username)) {
            object.put("valid", false);
            object.put("message", "账号名称不能为空");
            return object;
        }
        if (StringUtils.isNumeric(username)) {
            object.put("valid", false);
            object.put("message", "账号名称不能全是数字");
            return object;
        }
        if (username.length() < 6 || username.length() > 20) {
            object.put("valid", false);
            object.put("message", "账号名长度在6-20");
            return object;
        }
        AdminUsers adminUsers = adminUsersDao.findByUsername(username);
        if (adminUsers == null) {
            object.put("valid", true);
            object.put("message", "账号名称可用");
            return object;
        } else {
            object.put("valid", false);
            object.put("message", "账号已存在");
            return object;
        }
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_USER_ADD
    })
    @RequestMapping("checkPassword")
    @ResponseBody
    public JSONObject checkPasswordFormat(String password) {
        JSONObject object = new JSONObject();
        if (StringUtils.isBlank(password)) {
            object.put("valid", false);
            object.put("message", "密码不能为空");
            return object;
        }
        if (StringUtils.isNumeric(password)) {
            object.put("valid", false);
            object.put("message", "密码不能全是数字");
            return object;
        }
        if (password.length() < 6 || password.length() > 20) {
            object.put("valid", false);
            object.put("message", "密码长度在6-20");
            return object;
        }

        object.put("valid", true);
        object.put("message", "验证通过");
        return object;
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_USER_ADD
    })
    @RequestMapping("checkEmail")
    @ResponseBody
    public JSONObject checkEmail(String email) {
        JSONObject object = new JSONObject();
        if (StringUtils.isEmpty(email)) {
            object.put("valid", false);
            object.put("message", "email不能为空");
            return object;
        }

        if (!emailPattern.matcher(email).find()) {
            object.put("valid", false);
            object.put("message", "email格式不正确");
            return object;
        }

        AdminUsers user = adminUsersDao.findByEmail(email);
        if (user != null) {
            object.put("valid", false);
            object.put("message", "邮箱已存在");
            return object;
        }

        object.put("valid", true);
        object.put("message", "验证通过");
        return object;
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_AUTHORITY_USER_ADD
    })
    @RequestMapping("checkMobile")
    @ResponseBody
    public JSONObject checkMobile(String phone) {
        JSONObject object = new JSONObject();
        if (StringUtils.isEmpty(phone)) {
            object.put("valid", true);
            object.put("message", "验证通过");
            return object;
        }
        if (!mobilePattern.matcher(phone).find()) {
            object.put("valid", false);
            object.put("message", "手机号格式不正确");
            return object;
        }
        object.put("valid", true);
        object.put("message", "验证通过");
        return object;
    }
}
