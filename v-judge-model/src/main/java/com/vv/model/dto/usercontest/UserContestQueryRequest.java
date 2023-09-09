package com.vv.model.dto.usercontest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.vv.oj.common.PageRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询请求
 *
 * @author vv
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserContestQueryRequest extends PageRequest implements Serializable {

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
     * 比赛状态 0-正在参加（不显示） 1-结束（显示）
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}