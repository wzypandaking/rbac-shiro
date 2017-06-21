package org.springframework.rbac.shiro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.rbac.util.RSAUtil;
import org.springframework.rbac.util.SerializeUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.util.*;

/**
 * Created by pandaking on 2017/5/25.
 */
public class RestAuthRealm extends AuthorizingRealm implements InitializingBean {

    @Value("${rbac.api}")
    private String rbacShiroUrl;

    private final String rbacLicenseFile = "rbac.lic";
    private PublicKey publicKey;
    private String licenseKey;
    private String uuid;

    private String request(String api, Map<String, Object> param) {
        String url = String.format("%s/client/api/%s", rbacShiroUrl, api);
        try {
            Connection.Response response = Jsoup.connect(url)
                    .method(Connection.Method.POST)
                    .timeout(300)
                    .data(ImmutableMap.of(
                    "code", RSAUtil.encrypt(param, publicKey),
                    "uuid", uuid,
                    "licenseKey", licenseKey
                    )).ignoreContentType(true)
                    .execute();
            JSONObject object = JSONObject.parseObject(response.body(), JSONObject.class);
            if (!object.getBoolean("success")) {
                throw new AuthenticationException(object.getString("message"));
            }
            byte[] jsonBytes = RSAUtil.decrypt(Base64.getDecoder().decode(object.getString("data")), publicKey);
            return new String(jsonBytes, "UTF-8");
        } catch (Exception e) {
            throw new AuthenticationException("请求异常");
        }
    }

    /**
     * 登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        Map<String, Object> param = new HashMap<>();
        param.put("username", username);
        param.put("password", password);
        String result = request("auth/check", param);
        RbacProfile profile = JSON.parseObject(result, RbacProfile.class);
        return new SimpleAuthenticationInfo(profile, "", this.getClass().getName());
    }

    /**
     * 给登录用户授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        RbacProfile profile = (RbacProfile) principalCollection.fromRealm(this.getClass().getName()).iterator().next();
        Map<String, Object> param = new HashMap<>();
        param.put("uuid", profile.getUuid());
        String result = request("auth/permission", param);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        AuthInfo authInfo = JSON.parseObject(result, new TypeReference<AuthInfo>(){}.getType());
        Set<String> permissions = new HashSet<>();
        permissions.addAll(authInfo.getPermissions());
        info.addStringPermissions(permissions);

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.setAttribute("authInfo", authInfo);
        return info;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            ClassPathResource resource = new ClassPathResource(rbacLicenseFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            uuid = reader.readLine();
            licenseKey = reader.readLine();
            publicKey = SerializeUtil.deserialize(Base64.getDecoder().decode(reader.readLine()), PublicKey.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(rbacLicenseFile + " is not found");
        } catch (IOException e) {
            throw new RuntimeException(rbacLicenseFile + " must have read access");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
