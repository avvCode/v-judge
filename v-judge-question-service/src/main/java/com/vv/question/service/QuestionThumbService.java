package com.vv.question.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.model.entity.QuestionThumb;
import com.vv.model.entity.User;


/**
* @author zyz19
* @description 针对表【question_thumb(题目点赞)】的数据库操作Service
* @createDate 2023-08-18 18:25:54
*/
public interface QuestionThumbService extends IService<QuestionThumb> {
    /**
     * 点赞
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    int doQuestionThumb(long questionId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
    int doQuestionThumbInner(long userId, long questionId);
}
