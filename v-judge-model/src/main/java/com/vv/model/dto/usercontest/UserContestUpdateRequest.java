package com.vv.model.dto.usercontest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 更新请求
 *
 * @author vv
 */
@Data
public class UserContestUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 创建用户id
     */
    private Long userId;

    /**
     * 赛事id
     */
    private Long contestId;

    /**
     * AC数
     */
    private Integer acceptNum;

    /**
     * 总提交数
     */
    private Integer total;

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 总耗时
     */
    private String totalTime;

    /**
     * 比赛状态 0-正在参加（不显示） 1-结束（显示）
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}