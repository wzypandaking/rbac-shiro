package rbac.web.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by pandaking on 2017/5/24.
 */
@Setter
@Getter
public class ChooseUserVO {

    private String uuid;

    private boolean choose;

    private String username;
}
