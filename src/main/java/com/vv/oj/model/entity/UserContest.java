package com.vv.oj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户参赛记录表
 * @TableName user_contest
 */
@TableName(value ="user_contest")
@Data
public class UserContest implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建用 id
     */
    private Long userId;

    /**
     * 赛事id
     */
    private Long contestId;

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

    /**
     * Ac数
     */
    private Integer acceptNum;

    /**
     * 总提交数
     */
    private Integer total;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}