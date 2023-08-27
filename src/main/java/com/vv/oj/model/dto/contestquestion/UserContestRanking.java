package com.vv.oj.model.dto.contestquestion;

import lombok.Data;

/**
 * 对于某一道题目，用户的信息
 * @author vv
 */
@Data
public class UserContestRanking {
    /**
     * 题目的displayId
     */
    private String displayId;
    /**
     * 对该题的总提交数
     */
    private Integer total;

    /**
     * 是否是第一位
     */
    private boolean isFirst;

    private boolean isAc;

    /**
     * WA数
     */
    private Integer wrongNum;

    /**
     * 题目总耗时
     * 这个需要以最后AC的时间为准
     * 错一道题会有罚时
     */
    private String totalTime;
}
