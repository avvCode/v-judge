package com.vv.oj.model.dto.questionfavour;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目收藏 / 取消收藏请求
 *
 * @author vv
 */
@Data
public class QuestionFavourAddRequest implements Serializable {

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}