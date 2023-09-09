package com.vv.model.vo;

import cn.hutool.json.JSONUtil;
import com.vv.oj.model.dto.question.JudgeConfig;
import com.vv.oj.model.entity.ContestQuestion;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @author vv
 */
@Data
public class ContestQuestionVO {
    /**
     * id
     */
    private Long id;

    /**
     * 展现时题目名称
     */
    private String displayId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建题目人的信息
     */
    private UserVO userVO;

    /**
     * 题目难度 0-简单 1-中等 2-困难
     */
    private Integer rate;
    
    /**
     * 包装类转对象
     *
     * @param contestQuestionVO
     * @return
     */
    public static ContestQuestion voToObj(ContestQuestionVO contestQuestionVO) {
        if (contestQuestionVO == null) {
            return null;
        }
        ContestQuestion contestQuestion = new ContestQuestion();
        BeanUtils.copyProperties(contestQuestionVO, contestQuestion);
        List<String> tagList = contestQuestionVO.getTags();
        if (tagList != null) {
            contestQuestion.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig voJudgeConfig = contestQuestionVO.getJudgeConfig();
        if (voJudgeConfig != null) {
            contestQuestion.setJudgeConfig(JSONUtil.toJsonStr(voJudgeConfig));
        }
        return contestQuestion;
    }

    /**
     * 对象转包装类
     *
     * @param contestQuestion
     * @return
     */
    public static ContestQuestionVO objToVo(ContestQuestion contestQuestion) {
        if (contestQuestion == null) {
            return null;
        }
        ContestQuestionVO contestQuestionVO = new ContestQuestionVO();
        BeanUtils.copyProperties(contestQuestion, contestQuestionVO);
        List<String> tagList = JSONUtil.toList(contestQuestion.getTags(), String.class);
        contestQuestionVO.setTags(tagList);
        String judgeConfigStr = contestQuestion.getJudgeConfig();
        contestQuestionVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));
        return contestQuestionVO;
    }

    private static final long serialVersionUID = 1L;
}
