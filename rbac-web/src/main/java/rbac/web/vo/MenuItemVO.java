package rbac.web.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * Created by pandaking on 2017/5/24.
 */
@Setter
@Getter
public class MenuItemVO {

    private Long id;

    private Long pid;

    private String ico;

    private String name;

    private String mca;

    private String orderNumber;

    private String rule;
}
