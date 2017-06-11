package rbac.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.*;
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

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private AdminUsersService adminUsersService;
    @Autowired
    private AdminUsersDao adminUsersDao;
    @Autowired
    private AdminDepartmentService adminDepartmentService;

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

    @RequestMapping(value = "user")
    @ResponseBody
    public Result<ProfileVO> user(HttpServletRequest request) {
        AdminUsers adminUsers = adminUsersDao.findById((Long) request.getSession().getAttribute("id"));
        return Result.wrapResult(BeanUtil.copy(adminUsers, ProfileVO.class));
    }

    @RequestMapping("profile")
    @ResponseBody
    public Result<ProfileManageVO> profile(HttpServletRequest request) {
        AdminUsers adminUsers = adminUsersDao.findById((Long) request.getSession().getAttribute("id"));
        return Result.wrapResult(BeanUtil.copy(adminUsers, ProfileManageVO.class));
    }

    @RequestMapping(value = "change_password", method = RequestMethod.POST)
    @ResponseBody
    public Result changePassword(UserChangePassword password, HttpServletRequest request) {
        AdminUsers adminUsers = adminUsersDao.findById((Long) request.getSession().getAttribute("id"));
        if (adminUsers == null) {
            return Result.wrapResult(AdminUsersLang.NOT_FOUND);
        }
        AdminUsers user = adminUsersService.checkUserAndPassword(adminUsers.getUsername(), password.getPassword().get(0));
        if (user == null) {
            return Result.wrapResult(AdminUsersLang.PASSWORD_ERROR);
        }
        if (StringUtils.isEmpty(password.getPassword().get(1)) || ! password.getPassword().get(1).equals(password.getPassword().get(2))) {
            return Result.wrapResult(AdminUsersLang.PASSWORD_CHECK_FAILED);
        }
        return adminUsersService.changePassword(user, password.getPassword().get(1));
    }

    @RequestMapping("avatar")
    public byte[] loadAvatar(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("id");
        AdminUsers adminUsers = adminUsersDao.findById(id);
        return adminUsersService.getUserAvatar(adminUsers);
    }

    @RequestMapping(value = "profile/avatar", method = RequestMethod.POST)
    @ResponseBody
    public Result profileAvatar(MultipartRequest request) {
        MultipartFile multipartFile = request.getFile("Filedata");
        String folderName = String.format("%s/avatar", uploadPath);
        // 创建folder
        {
            File folder = new File(folderName);
            if (!folder.exists() || !folder.isDirectory()) {
                if (!folder.mkdirs()) {
                    log.error("创建文件夹失败 {}", folderName);
                    return Result.wrapResult(SystemLang.ERROR);
                }
            }
        }
        String filename = String.format("%s/%s_%s", folderName, System.currentTimeMillis(), multipartFile.getOriginalFilename());
        File file = new File(filename);
        byte data[];
        try {
            InputStream stream = new BufferedInputStream(multipartFile.getInputStream());
            data = new byte[stream.available()];
            stream.read(data);
            stream.close();

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();

            {
                HttpServletRequest r = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                AdminUsers user =  adminUsersDao.findById((Long)r.getSession().getAttribute("id"));
                if (user == null) {
                    throw new RuntimeException("没有找到用户信息");
                }
                user.setAvatar(filename);
                adminUsersDao.save(user);
            }
        } catch (Exception e) {
            return Result.wrapResult(SystemLang.ERROR);
        }
        return Result.wrapResult("admin/avatar");
    }


    @RequestMapping(value = "logout")
    @ResponseBody
    public Result logout(HttpServletRequest request) {
        SecurityUtils.getSubject().logout();
        request.getSession().setAttribute("id", null);
        request.getSession().setAttribute("managedUids", null);
        return Result.wrapResult(LoginLang.LOGOUT_SUCCESS);
    }

}
