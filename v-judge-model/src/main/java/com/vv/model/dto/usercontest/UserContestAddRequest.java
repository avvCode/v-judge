package com.vv.model.dto.usercontest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 创建请求
 *
 * @author vv
 */
@Data
public class UserContestAddRequest implements Serializable {

    /**
     * 赛事id
     */
    private Long contestId;

    /**
     * 创建用户id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}