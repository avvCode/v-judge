package com.vv.oj.model.dto.contestquestionsubmit;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.vv.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContestQuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

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
     * 判题结果 0-10
     */
    private Integer result;

    private static final long serialVersionUID = 1L;
}