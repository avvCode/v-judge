package com.vv.oj.model.enums;

/**
 * @author vv
 */
public enum ContestStatusEnum {
    BANDED(0,"禁用"),
    OPEN(1,"启用"),
    ING(2, "进行中"),
    END(3,"结束")
    ;

    ContestStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    int code;
    String description;
}
