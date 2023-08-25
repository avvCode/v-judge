package com.vv.oj.model.dto.contestquestionsubmit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建请求
 *
 */
@Data
public class ContestQuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 判断信息（json对象）
     */
    private String judgeInfo;

    /**
     * 判题状态
     */
    private Integer status;

    /**
     * 赛事题目id
     */
    private Long contestQuestionId;

    /**
     * 赛事id
     */
    private Long contestId;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 用户代码
     */
    private String code;


    private static final long serialVersionUID = 1L;
}