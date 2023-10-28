package com.imooc.pan.server.modules.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * chunk merge enums
 */
@Getter
@AllArgsConstructor
public enum MergeFlagEnum {

    /**
     * no merge
     */
    NOT_READY(0),

    /**
     * merge needed
     */
    READY(1);

    private Integer code;

}
