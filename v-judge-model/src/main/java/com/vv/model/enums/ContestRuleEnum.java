package com.vv.model.enums;

import lombok.Getter;

/**
 * @author vv
 */
@Getter
public enum ContestRuleEnum {
    ACM(0,"ACM"),
    OI(1,"OI"),
    ;

    ContestRuleEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    final int code;
    final String description;
}
