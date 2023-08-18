package com.vv.oj.model.dto.contest;

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
 * @author vv
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContestQueryRequest extends PageRequest implements Serializable {

    /**
     * 赛事id
     */
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
     * 赛事状态 0-锁定 1-正在进行 2-结束
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}