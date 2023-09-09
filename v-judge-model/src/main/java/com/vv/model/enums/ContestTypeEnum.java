package com.vv.model.enums;

import lombok.Getter;

/**
 * @author vv
 */
@Getter
public enum ContestTypeEnum {
    PUBLIC(0,"public"),
    PRIVATE(1,"private"),
    ;

    ContestTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    final int code;
    final String description;
}
