package com.vv.model.codesandbox;

import lombok.Data;

/**
 * @author vv
 */
@Data
public class JudgeInfo {
    /**
     * 程序执行信息
     */
    private Integer code;

    /**
     * 消耗内存（KB）
     */
    private Long memory;

    /**
     * 消耗时间（ms）
     */
    private Long time;
}
