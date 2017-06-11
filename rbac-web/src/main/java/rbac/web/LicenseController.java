package rbac.web;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rbac.RbacPermissions;
import rbac.dao.AdminVersionDao;
import rbac.dao.AdminVersionLicenseDao;
import rbac.dao.repository.AdminVersion;
import rbac.dao.repository.AdminVersionLicense;
import rbac.service.AdminVersionLicenseService;
import rbac.service.AdminVersionService;
import rbac.utils.BeanUtil;
import rbac.utils.PagingResult;
import rbac.utils.Result;
import rbac.web.lang.AdminVersionLang;
import rbac.web.lang.AdminVersionLicenseLang;
import rbac.web.param.PageParam;
import rbac.web.param.VersionSearchParam;
import rbac.web.shiro.RequiresPermissions;
import rbac.web.vo.AdminLicenseDetailVO;
import rbac.web.vo.AdminLicenseVO;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/19
 * @note: 请补充说明
 * @history:
 */
@RestController
@RequestMapping("license")
public class LicenseController {

    @Autowired
    private AdminVersionLicenseService adminVersionLicenseService;
    @Autowired
    private AdminVersionService adminVersionService;
    @Autowired
    private AdminVersionDao adminVersionDao;
    @Autowired
    private AdminVersionLicenseDao adminVersionLicenseDao;

    @RequiresPermissions({
            RbacPermissions.RBAC_VC_LICENSE
    })
    @RequestMapping("list")
    @ResponseBody
    public PagingResult<AdminLicenseVO> list(VersionSearchParam param, PageParam pageParam) {
        PagingResult<AdminVersionLicense> result = adminVersionLicenseService.search(param, pageParam);
        List<AdminLicenseVO> list = BeanUtil.copy(result.getList(), new TypeReference<List<AdminLicenseVO>>() {
        }.getType());
        return PagingResult.wrapResult(list, result.getTotal(), result.getPage(), result.getSize());
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_VC_LICENSE_STATUS
    })
    @RequestMapping(value = "toggleStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result toggleStatus(String uuid, Integer status) {
        AdminVersionLicense license = adminVersionLicenseDao.findByUuid(uuid);
        Result result = adminVersionLicenseService.checkLicense(license);
        if (!result.isSuccess()) {
            return result;
        }
        return adminVersionLicenseService.toggleStatus(license, status);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_VC_LICENSE_VIEW
    })
    @RequestMapping(value = "detail")
    @ResponseBody
    public Result detail(String uuid) {
        AdminVersionLicense license = adminVersionLicenseDao.findByUuid(uuid);
        Result result = adminVersionLicenseService.checkLicense(license);
        if (!result.isSuccess()) {
            return result;
        }
        return Result.wrapResult(BeanUtil.copy(license, AdminLicenseDetailVO.class));
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_VC_LICENSE_VIEW
    })
    @RequestMapping("download")
    public void download(String uuid, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition","attachment;  filename=license" + DateTime.now().toString("yyyyMMddHHmmss") + ".lic");
        response.setContentType("application/octet-stream;charset=UTF-8");

        AdminVersionLicense license = adminVersionLicenseDao.findByUuid(uuid);
        Result result = adminVersionLicenseService.checkLicense(license);
        if (!result.isSuccess()) {
            throw new RuntimeException(result.getMessage());
        }
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(license.getLicense().getBytes("UTF-8"));
        outputStream.write("\n".getBytes("UTF-8"));
        outputStream.write(license.getPublicKey().getBytes("UTF-8"));
        outputStream.flush();
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_VC_VERSION_LICENSE_ADD
    })
    @RequestMapping("create")
    @ResponseBody
    public Result create(String versionUuid, Integer expire, Integer licenseNum) {
        AdminVersion adminVersion = adminVersionDao.findByUuid(versionUuid);
        if (adminVersion == null) {
            return Result.wrapResult(AdminVersionLang.NOT_FOUND);
        }
        if (expire == null) {
            expire = 1;
        }
        if (licenseNum == null) {
            licenseNum = 1;
        }
        return adminVersionService.addVersionLicense(adminVersion, expire, licenseNum);
    }
}
