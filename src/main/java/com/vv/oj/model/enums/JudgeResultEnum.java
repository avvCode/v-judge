package com.vv.oj.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author vv
 */
@Getter
public enum JudgeResultEnum {
    ACCEPTED("Accepted",  0),
    WRONG_ANSWER("Wrong Answer",  1),
    COMPILE_ERROR("Compile Error",  2),
    MEMORY_LIMIT_EXCEEDED("Memory_Limit_exceeded",  3),
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded",  4),
    PRESENTATION_ERROR("Presentation Error",  5),
    WAITING("Waiting", 6),
    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded",  7),
    DANGEROUS_OPERATION("Dangerous Operation",  8),
    RUNTIME_ERROR("Runtime Error",  9),
    SYSTEM_ERROR("System Error",  10);


    private final String msg;

    private final int code;

    JudgeResultEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}
