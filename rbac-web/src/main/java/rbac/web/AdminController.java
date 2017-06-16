package rbac.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import rbac.dao.AdminUsersDao;
import rbac.dao.repository.AdminUsers;
import rbac.service.AdminDepartmentService;
import rbac.service.AdminUsersService;
import rbac.service.UploadService;
import rbac.utils.BeanUtil;
import rbac.utils.Result;
import rbac.web.lang.AdminUsersLang;
import rbac.web.lang.LoginLang;
import rbac.web.lang.SystemLang;
import rbac.web.param.LoginParam;
import rbac.web.param.UserChangePassword;
import rbac.web.vo.ProfileManageVO;
import rbac.web.vo.ProfileVO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/15
 * @note: 请补充说明
 * @history:
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminUsersService adminUsersService;
    @Autowired
    private AdminUsersDao adminUsersDao;
    @Autowired
    private AdminDepartmentService adminDepartmentService;
    @Autowired
    private UploadService uploadService;

    /**
     * 登录
     * @param param
     * @param request
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(LoginParam param, HttpServletRequest request) {
        AdminUsers user = adminUsersService.checkUserAndPassword(param.getUsername(), param.getPassword());
        Result result = adminUsersService.checkUser(user);
        if (!result.isSuccess()) {
            return result;
        }
        request.getSession().setAttribute("id", user.getId());
        Set<Long> managerUids = adminDepartmentService.getManagedUidByUserId(user.getId());
        managerUids.add(user.getId());
        request.getSession().setAttribute("managedUids", managerUids);
        //  权限
        {
            Result<Set<String>> rulesResult = adminUsersService.getRulesByUser(user);
            Set<String> rules = new HashSet<>();
            if (rulesResult.isSuccess()) {
                rules = rulesResult.getData();
            }
            request.getSession().setAttribute("rules", rules);
        }

        adminUsersService.userLogin(user);
        // shiro 登录
        UsernamePasswordToken token = new UsernamePasswordToken(param.getUsername(), param.getPassword());
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        return Result.wrapResult(LoginLang.SUCCESS);
    }

    /**
     * 加载头像信息
     * @param request
     * @return
     */
    @RequestMapping(value = "user")
    @ResponseBody
    public Result<ProfileVO> user(HttpServletRequest request) {
        AdminUsers adminUsers = adminUsersDao.findById((Long) request.getSession().getAttribute("id"));
        return Result.wrapResult(BeanUtil.copy(adminUsers, ProfileVO.class));
    }

    /**
     * 加载用户信息
     * @param uuid
     * @param request
     * @return
     */
    @RequestMapping("profile")
    @ResponseBody
    public Result<ProfileManageVO> profile(String uuid, HttpServletRequest request) {
        AdminUsers adminUsers;
        if (StringUtils.isEmpty(uuid)) {
            adminUsers = adminUsersDao.findById((Long) request.getSession().getAttribute("id"));
        } else {
            adminUsers = adminUsersDao.findByUuid(uuid);
        }
        return Result.wrapResult(BeanUtil.copy(adminUsers, ProfileManageVO.class));
    }

    /**
     * 更换密码
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "change_password", method = RequestMethod.POST)
    @ResponseBody
    public Result changePassword(UserChangePassword password, HttpServletRequest request) {
        AdminUsers user;
        if (StringUtils.isEmpty(password.getUuid())) {
            AdminUsers adminUsers = adminUsersDao.findById((Long) request.getSession().getAttribute("id"));
            user = adminUsersService.checkUserAndPassword(adminUsers.getUsername(), password.getPassword().get(0));
            if (user == null) {
                return Result.wrapResult(AdminUsersLang.NOT_FOUND);
            }
            if (StringUtils.isEmpty(password.getPassword().get(1))
                    || ! password.getPassword().get(1).equals(password.getPassword().get(2))) {
                return Result.wrapResult(AdminUsersLang.PASSWORD_CHECK_FAILED);
            }
        } else {
            user = adminUsersDao.findByUuid(password.getUuid());
            if (user == null) {
                return Result.wrapResult(AdminUsersLang.NOT_FOUND);
            }
            if (StringUtils.isEmpty(password.getPassword().get(0))
                    || ! password.getPassword().get(1).equals(password.getPassword().get(1))) {
                return Result.wrapResult(AdminUsersLang.PASSWORD_CHECK_FAILED);
            }
        }
        return adminUsersService.changePassword(user, password.getPassword().get(1));
    }

    /**
     * 加载头像信息
     * @param request
     * @return
     */
    @RequestMapping("avatar")
    public byte[] loadAvatar(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("id");
        AdminUsers adminUsers = adminUsersDao.findById(id);
        return adminUsersService.getUserAvatar(adminUsers);
    }

    /**
     * 保存头像
     * @param avatar
     * @param uuid
     * @return
     */
    @RequestMapping("avatar/save")
    @ResponseBody
    public Result saveAvatar(String avatar, String uuid) {
        AdminUsers user;
        if (StringUtils.isNotEmpty(uuid)) {
            user = adminUsersDao.findByUuid(uuid);
        } else {
            HttpServletRequest r = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            user = adminUsersDao.findById((Long) r.getSession().getAttribute("id"));
        }
        if (user == null) {
            throw new RuntimeException("没有找到用户信息");
        }
        user.setAvatar(avatar);
        adminUsersDao.save(user);
        return Result.wrapResult(SystemLang.SUCCESS);
    }

    /**
     * 上传头像
     * @param request
     * @return
     */
    @RequestMapping(value = "profile/avatar", method = RequestMethod.POST)
    @ResponseBody
    public Result profileAvatar(MultipartRequest request) {
        MultipartFile multipartFile = request.getFile("Filedata");
        String filename = String.format("%s_%s", System.currentTimeMillis(), multipartFile.getOriginalFilename());
        Result result = Result.wrapResult(SystemLang.ERROR);
        try {
            result = adminUsersService.saveUserAvatar(filename, multipartFile.getInputStream());
        } catch (IOException e) {
            log.error("上传头像失败", e);
        }
        return result;
    }

    @RequestMapping(value = "files")
    public byte[] getUploadFile(String filename){
        try {
            return uploadService.getUploadFile(filename);
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * 退出
     * @param request
     * @return
     */
    @RequestMapping(value = "logout")
    @ResponseBody
    public Result logout(HttpServletRequest request) {
        SecurityUtils.getSubject().logout();
        request.getSession().setAttribute("id", null);
        request.getSession().setAttribute("managedUids", null);
        return Result.wrapResult(LoginLang.LOGOUT_SUCCESS);
    }

}
