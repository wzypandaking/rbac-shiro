package rbac.web.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by pandaking on 2017/5/26.
 */
@Setter
@Getter
public class ChooseRuleVO {

    private Long id;

    private Long pid;

    private String uuid;

    private String title;

    private boolean choose;
}
