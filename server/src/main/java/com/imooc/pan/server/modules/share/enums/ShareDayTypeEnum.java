package com.imooc.pan.server.modules.share.enums;

import com.imooc.pan.core.constants.driveHarborConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * share day type enums
 */
@AllArgsConstructor
@Getter
public enum ShareDayTypeEnum {

    PERMANENT_VALIDITY(0, 0, "Valid Permanently"),
    SEVEN_DAYS_VALIDITY(1, 7, "Valid for 7 Days"),
    THIRTY_DAYS_VALIDITY(2, 30, "Valid for 30 Days");

    private Integer code;

    private Integer days;

    private String desc;

    /**
     * get the days based on the code
     *
     * @param code
     * @return
     */
    public static Integer getShareDayByCode(Integer code) {
        if (Objects.isNull(code)) {
            return driveHarborConstants.MINUS_ONE_INT;
        }
        for (ShareDayTypeEnum value : values()) {
            if (Objects.equals(value.getCode(), code)) {
                return value.getDays();
            }
        }
        return driveHarborConstants.MINUS_ONE_INT;
    }

}
