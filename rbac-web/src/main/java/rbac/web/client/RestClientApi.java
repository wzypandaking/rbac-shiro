package rbac.web.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.rbac.util.DESUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import rbac.dao.AdminAuthGroupAccessDao;
import rbac.dao.AdminUsersDao;
import rbac.dao.AdminVersionLicenseDao;
import rbac.dao.repository.AdminAuthGroupAccess;
import rbac.dao.repository.AdminUsers;
import rbac.dao.repository.AdminVersionLicense;
import rbac.utils.RSAUtil;
import rbac.utils.Result;
import rbac.utils.SerializeUtil;
import rbac.web.lang.AdminVersionLicenseLang;
import rbac.web.lang.SystemLang;
import rbac.web.vo.client.ClientVO;

import java.net.URLDecoder;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pandaking on 2017/5/31.
 */
@Slf4j
@RequestMapping("client/api")
public abstract class RestClientApi {

    @Autowired
    private AdminVersionLicenseDao adminVersionLicenseDao;
    @Autowired
    private AdminUsersDao adminUsersDao;
    @Autowired
    private AdminAuthGroupAccessDao adminAuthGroupAccessDao;

    protected Result<JSONObject> checkParam(ClientApiRequestParam param) {
        if (StringUtils.isBlank(param.getLicenseKey())) {
            return Result.wrapResult(AdminVersionLicenseLang.NOT_FOUND);
        }
        {
            if (StringUtils.isEmpty(param.getUuid())) {
                log.error("构建的uuid不存在");
                return Result.wrapResult(SystemLang.PARAM_WRONG);
            }
            AdminUsers buildUser = adminUsersDao.findByUuid(param.getUuid());
            if (buildUser == null) {
                log.error("构建的uuid不存在");
                return Result.wrapResult(SystemLang.PARAM_WRONG);
            }
            AdminAuthGroupAccess access = adminAuthGroupAccessDao.findByUidAndGroupId(buildUser.getId(), 2L);
            if (access == null) {
                log.error("license被修改了");
                return Result.wrapResult(SystemLang.PARAM_WRONG);
            }
        }

        AdminVersionLicense license = adminVersionLicenseDao.findByLicense(param.getLicenseKey());
        if (license == null) {
            return Result.wrapResult(AdminVersionLicenseLang.NOT_FOUND);
        }
        if (license.getStatus() != 1) {
            return Result.wrapResult(AdminVersionLicenseLang.ILLEGAL_STATUS);
        }
        if (DateTime.now().isAfter(license.getExpireTime().getTime())) {
            return Result.wrapResult(AdminVersionLicenseLang.LICENSE_HAD_EXPIRE);
        }
        try {
            PrivateKey privateKey = SerializeUtil.deserialize(Base64.getDecoder().decode(license.getPrivateKey()), PrivateKey.class);
            JSONObject object = JSON.parseObject(RSAUtil.decrypt(Base64.getDecoder().decode(URLDecoder.decode(param.getCode(), "UTF-8")), privateKey), JSONObject.class);
            object.put("license", license);
            return Result.wrapResult(object);
        } catch (Exception e) {
            log.error("用户登录解析错误 param={}", JSON.toJSONString(param));
        }
        return Result.wrapResult(SystemLang.ERROR);
    }


    protected Result response(AdminVersionLicense license, Map<String, Object> result) {
        try {
            //  使用DES加密原始数据
            String json = JSON.toJSONString(result);
            String key = DESUtil.buildSecurityKey();
            String encryptString = DESUtil.encrypt(json, key);

            //  使用RSA加密秘钥
            PrivateKey privateKey = SerializeUtil.deserialize(Base64.getDecoder().decode(license.getPrivateKey()), PrivateKey.class);
            String encryptKey = Base64.getEncoder().encodeToString(RSAUtil.encrypt(key.getBytes(), privateKey));

            ClientVO client = new ClientVO();
            client.setCode(encryptString);
            client.setKey(encryptKey);
            return Result.wrapResult(JSON.toJSONString(client));
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }
}
