package rbac.web.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import rbac.dao.repository.AdminUsers;
import rbac.utils.Md5Util;

/**
 * Created by pandaking on 2017/5/25.
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo info) {
        return true;
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        String password = new String(token.getPassword());
//        AdminUsers user = (AdminUsers) info.getPrincipals().asList().iterator().next();
//        String pwd = Md5Util.md5(Md5Util.md5(password) + user.getSalt());
//        return user.getPassword().equals(pwd);
    }
}
