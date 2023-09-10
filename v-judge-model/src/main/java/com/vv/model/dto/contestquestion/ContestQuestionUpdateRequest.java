package com.vv.model.dto.contestquestion;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.vv.model.dto.question.JudgeCase;
import com.vv.model.dto.question.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *
 * @author vv
 */
@Data
public class ContestQuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 赛事id
     */
    private Long contestId;

    /**
     * 展现时题目名称
     */
    private String displayId;

    /**
     * 题目
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置（json对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 题目难度 0-简单 1-中等 2-困难
     */
    private Integer rate;

    private static final long serialVersionUID = 1L;
}