package com.vv.oj.judge;

import com.vv.oj.judge.strategy.DefaultJudgeStrategy;
import com.vv.oj.judge.strategy.JavaLanguageJudgeStrategy;
import com.vv.oj.judge.strategy.JudgeContext;
import com.vv.oj.judge.strategy.JudgeStrategy;
import com.vv.oj.model.dto.questionsubmit.JudgeInfo;
import com.vv.oj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
