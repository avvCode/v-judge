package com.vv.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author vv
 */
@Getter
public enum JudgeInfoMessageEnum {
    ACCEPTED("Accepted", "成功", 0),
    WRONG_ANSWER("Wrong Answer", "答案错误", 1),
    COMPILE_ERROR("Compile Error", "编译错误", 2),
    MEMORY_LIMIT_EXCEEDED("Memory_Limit_exceeded", "内存溢出", 3),
    TIME_LIMIT_EXCEEDED("Time Limit Exceeded", "超时", 4),
    PRESENTATION_ERROR("Presentation Error", "展示错误", 5),
    WAITING("Waiting", "等待中", 6),
    OUTPUT_LIMIT_EXCEEDED("Output Limit Exceeded", "输出溢出", 7),
    DANGEROUS_OPERATION("Dangerous Operation", "危险操作", 8),
    RUNTIME_ERROR("Runtime Error", "运行错误", 9),
    SYSTEM_ERROR("System Error", "系统错误", 10);

    private final String text;

    private final String value;

    private final int code;

    JudgeInfoMessageEnum(String text, String value, int code) {
        this.text = text;
        this.value = value;
        this.code = code;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
