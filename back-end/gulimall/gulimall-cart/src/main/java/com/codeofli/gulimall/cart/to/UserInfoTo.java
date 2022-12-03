package com.codeofli.gulimall.cart.to;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class UserInfoTo {

    private Long userId;

    private String userKey;

    /**
     * 浏览器是否已有user-key
     */
    private Boolean tempUser = false;

}
