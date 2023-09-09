package com.vv.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 赛事表
 * @TableName contest
 */
@TableName(value ="contest")
@Data
public class Contest implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 赛事名称
     */
    private String title;

    /**
     * 赛事描述
     */
    private String description;

    /**
     * 赛事公告
     */
    private String announcements;

    /**
     * 赛事类型 0-公共 1-私有
     */
    private Integer type;

    /**
     * 赛事规则 0-ACM 1-OI
     */
    private Integer rules;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 参赛人数
     */
    private Integer joinNum;

    /**
     * 赛事状态 0-锁定 1-开启 2-正在进行 3-结束
     */
    private Integer status;

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