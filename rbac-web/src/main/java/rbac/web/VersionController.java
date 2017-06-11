package rbac.web;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rbac.RbacPermissions;
import rbac.dao.AdminVersionDao;
import rbac.dao.repository.AdminVersion;
import rbac.service.AdminVersionService;
import rbac.utils.BeanUtil;
import rbac.utils.PagingResult;
import rbac.utils.Result;
import rbac.web.lang.AdminVersionLang;
import rbac.web.param.AdminVersionAddParam;
import rbac.web.param.PageParam;
import rbac.web.param.SearchParam;
import rbac.web.shiro.RequiresPermissions;
import rbac.web.vo.AdminVersionVO;

import java.util.List;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/18
 * @note: 请补充说明
 * @history:
 */
@RestController
@RequestMapping("version")
public class VersionController {

    @Autowired
    private AdminVersionService adminVersionService;
    @Autowired
    private AdminVersionDao adminVersionDao;

    @RequiresPermissions({
            RbacPermissions.RBAC_VC_VERSION
    })
    @RequestMapping("list")
    @ResponseBody
    public PagingResult<AdminVersionVO> list(SearchParam param, PageParam pageParam) {
        PagingResult<AdminVersion> result = adminVersionService.search(param, pageParam);
        List<AdminVersionVO> list = BeanUtil.copy(result.getList(), new TypeReference<List<AdminVersionVO>>(){}.getType());
        return PagingResult.wrapResult(list,
                result.getTotal(),
                result.getPage(),
                result.getSize());
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_VC_VERSION
    })
    @RequestMapping("listAll")
    @ResponseBody
    public Result<List<AdminVersionVO>> listAll() {
        List<AdminVersionVO> list = BeanUtil.copy(adminVersionDao.findAll(), new TypeReference<List<AdminVersionVO>>(){}.getType());
        return Result.wrapResult(list);
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_VC_VERSION
    })
    @RequestMapping("item")
    @ResponseBody
    public Result<AdminVersionVO> item(String uuid) {
        AdminVersion version = adminVersionDao.findByUuid(uuid);
        if (version == null) {
            return Result.wrapResult(AdminVersionLang.NOT_FOUND);
        }
        return Result.wrapResult(BeanUtil.copy(version, AdminVersionVO.class));
    }

    @RequiresPermissions({
            RbacPermissions.RBAC_VC_VERSION_ADD
    })
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseBody
    public Result addVersion(AdminVersionAddParam param) {
        AdminVersion adminVersion = BeanUtil.copy(param, AdminVersion.class);
        return adminVersionService.addVersion(adminVersion);
    }
}
