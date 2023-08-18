package com.vv.oj.model.dto.contest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 编辑请求
 *
 * @author vv
 */
@Data
public class ContestEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 赛事描述
     */
    private String description;

    /**
     * 赛事名称
     */
    private String title;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;


    private static final long serialVersionUID = 1L;
}