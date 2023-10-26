package com.imooc.pan.server.modules.file.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FolderFlagEnum {
    NO(0),
    YES(1);
    private Integer code;
}
