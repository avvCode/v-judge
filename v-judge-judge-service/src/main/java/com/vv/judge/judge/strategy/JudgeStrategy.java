package com.vv.judge.judge.strategy;


import com.vv.judge.judge.strategy.model.JudgeContext;
import com.vv.model.codesandbox.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
