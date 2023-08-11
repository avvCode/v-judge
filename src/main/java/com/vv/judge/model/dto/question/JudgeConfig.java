package com.vv.judge.model.dto.question;

import lombok.Data;

/**
 * @author vv
 */
@Data
public class JudgeConfig {
    /**
     * 时间限制（ms）
     */
    private Long timeLimit;
    /**
     * 内存限制（KB）
     */
    private Long memoryLimit;
    /**
     * 堆栈限制（KB）
     */
    private Long stackLimit;
}
