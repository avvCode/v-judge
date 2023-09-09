package com.vv.model.dto.questionthumb;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目点赞请求
 *
 * @author vv
 */
@Data
public class QuestionThumbAddRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}