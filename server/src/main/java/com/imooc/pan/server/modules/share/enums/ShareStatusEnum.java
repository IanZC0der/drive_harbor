package com.imooc.pan.server.modules.share.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * share status enums
 */
@AllArgsConstructor
@Getter
public enum ShareStatusEnum {

    NORMAL(0, "Normal Status"),
    FILE_DELETED(1, "File Deleted");

    private Integer code;

    private String desc;

}
