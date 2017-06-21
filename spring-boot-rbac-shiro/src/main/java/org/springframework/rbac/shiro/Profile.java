package org.springframework.rbac.shiro;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by pandaking on 2017/5/31.
 */
@Setter
@Getter
public class Profile {

    private String uuid;

    private String username;

    private String avatar;
}