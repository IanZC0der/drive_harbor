package com.imooc.pan.server.modules.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * delete flag enums
 */
@AllArgsConstructor
@Getter
public enum DelFlagEnum {
    /**
     * not deleted
     */
    NO(0),
    /**
     * deleted
     */
    YES(1);

    private Integer code;
}
