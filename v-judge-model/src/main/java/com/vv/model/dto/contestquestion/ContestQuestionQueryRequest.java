package com.vv.model.dto.contestquestion;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.vv.oj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 *
 * @author vv
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ContestQuestionQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 展现时题目名称
     */
    private String displayId;

    /**
     * 赛事id
     */
    private Long contestId;


    /**
     * 题目
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目难度 0-简单 1-中等 2-困难
     */
    private Integer rate;

    /**
     * 创建用户 id
     */
    private Long userId;



    private static final long serialVersionUID = 1L;
}