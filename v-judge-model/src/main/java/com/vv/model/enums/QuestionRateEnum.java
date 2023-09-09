package com.vv.model.enums;

/**
 * 题目难易度枚举
 * @author vv
 */
public enum QuestionRateEnum {
    EASY(0,"简单"),
    MID(1,"中等"),
    HARD(2, "困难");

    ;

    QuestionRateEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    int code;
    String description;
}
