package com.vv.oj.judge.strategy;

import com.vv.oj.model.dto.question.JudgeCase;
import com.vv.oj.judge.codesandbox.model.JudgeInfo;
import com.vv.oj.model.entity.Question;
import com.vv.oj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
