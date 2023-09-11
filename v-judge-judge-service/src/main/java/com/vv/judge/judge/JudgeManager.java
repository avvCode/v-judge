package com.vv.judge.judge;

import com.vv.judge.judge.strategy.impl.DefaultJudgeStrategy;
import com.vv.judge.judge.strategy.impl.JavaLanguageJudgeStrategy;
import com.vv.judge.judge.strategy.model.JudgeContext;
import com.vv.judge.judge.strategy.JudgeStrategy;
import com.vv.model.codesandbox.JudgeInfo;
import com.vv.model.entity.QuestionSubmit;
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
