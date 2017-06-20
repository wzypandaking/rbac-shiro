package rbac.web.client;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by pandaking on 2017/5/31.
 */
@Setter
@Getter
public class ClientApiRequestParam {

    private String uuid;

    private String licenseKey;

    private String code;
}
