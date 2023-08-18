package com.vv.oj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.oj.model.entity.Question;
import com.vv.oj.model.entity.QuestionFavour;
import com.vv.oj.model.entity.User;

/**
* @author zyz19
* @description 针对表【question_favour(题目收藏)】的数据库操作Service
* @createDate 2023-08-18 18:25:54
*/
public interface QuestionFavourService extends IService<QuestionFavour> {
    /**
     * 題目收藏
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    int doQuestionFavour(long questionId, User loginUser);

    /**
     * 分页获取用户收藏的題目列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper,
                                    long favourUserId);

    /**
     * 題目收藏（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionFavourInner(long userId, long questionId);
}
