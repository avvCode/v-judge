package com.vv.judge.model.dto.question;

import lombok.Data;

/**
 * @author vv
 */
@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;
    /**
     * 输出用例
     */
    private String output;
}
