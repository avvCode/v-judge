package com.vv.oj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 题目提交
 * @TableName contest_question_submit
 */
@TableName(value ="contest_question_submit")
@Data
public class ContestQuestionSubmit implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 创建用 id
     */
    private Long userId;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}