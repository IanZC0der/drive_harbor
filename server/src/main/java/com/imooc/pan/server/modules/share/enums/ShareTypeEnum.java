package com.imooc.pan.server.modules.share.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * share type enums
 */
@AllArgsConstructor
@Getter
public enum ShareTypeEnum {
    NEED_SHARE_CODE(0, "Share password enabled");

    private Integer code;

    private String desc;
}
