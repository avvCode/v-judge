package com.vv.model.dto.questionfavour;

import com.vv.oj.common.PageRequest;
import com.vv.oj.model.dto.question.QuestionQueryRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 题目收藏查询请求
 *
 * @author vv
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionFavourQueryRequest extends PageRequest implements Serializable {

    /**
     * 帖子查询请求
     */
    private QuestionQueryRequest questionQueryRequest;

    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}