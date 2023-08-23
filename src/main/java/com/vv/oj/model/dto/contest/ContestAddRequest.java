package com.vv.oj.model.dto.contest;

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
 * @author vv
 */
@Data
public class ContestAddRequest implements Serializable {

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
     * 赛事状态 0-锁定 1-开启 2-正在进行 3-结束
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}