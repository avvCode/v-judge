package com.vv.model.vo;

import cn.hutool.json.JSONUtil;
import com.vv.model.codesandbox.JudgeInfo;
import com.vv.model.entity.ContestQuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author vv
 */
@Data
public class ContestQuestionSubmitVO {
    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 提交用户信息
     */
    private UserVO userVO;

    /**
     * 判题结果 0-10
     */
    private Integer result;

    /**
     * 对应题目信息
     */
    private ContestQuestionVO contestQuestionVO;

    /**
     * 包装类转对象
     *
     * @param contestQuestionSubmitVO
     * @return
     */
    public static ContestQuestionSubmit voToObj(ContestQuestionSubmitVO contestQuestionSubmitVO) {
        if (contestQuestionSubmitVO == null) {
            return null;
        }
        ContestQuestionSubmit contestQuestionSubmit = new ContestQuestionSubmit();
        BeanUtils.copyProperties(contestQuestionSubmitVO, contestQuestionSubmit);
        JudgeInfo judgeInfoObj = contestQuestionSubmitVO.getJudgeInfo();
        if (judgeInfoObj != null) {
            contestQuestionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoObj));
        }
        return contestQuestionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param contestQuestionSubmit
     * @return
     */
    public static ContestQuestionSubmitVO objToVo(ContestQuestionSubmit contestQuestionSubmit) {
        if (contestQuestionSubmit == null) {
            return null;
        }
        ContestQuestionSubmitVO contestQuestionSubmitVO = new ContestQuestionSubmitVO();
        BeanUtils.copyProperties(contestQuestionSubmit, contestQuestionSubmitVO);
        String judgeInfoStr = contestQuestionSubmit.getJudgeInfo();
        contestQuestionSubmitVO.setJudgeInfo(JSONUtil.toBean(judgeInfoStr, JudgeInfo.class));
        return contestQuestionSubmitVO;
    }

    private static final long serialVersionUID = 1L;
}
