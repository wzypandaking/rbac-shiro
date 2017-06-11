package rbac.web.param;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by pandaking on 2017/6/11.
 */
@Setter
@Getter
public class UserChangePassword {

    private List<String> password;
}
