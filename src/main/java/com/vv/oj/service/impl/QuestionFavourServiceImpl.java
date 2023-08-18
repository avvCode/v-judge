package com.vv.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.oj.common.ErrorCode;
import com.vv.oj.exception.BusinessException;
import com.vv.oj.mapper.QuestionFavourMapper;
import com.vv.oj.model.entity.Question;
import com.vv.oj.model.entity.QuestionFavour;
import com.vv.oj.model.entity.QuestionFavour;
import com.vv.oj.model.entity.User;
import com.vv.oj.service.QuestionFavourService;
import com.vv.oj.service.QuestionService;
import com.vv.oj.service.QuestionFavourService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author zyz19
* @description 针对表【question_favour(题目收藏)】的数据库操作Service实现
* @createDate 2023-08-18 18:25:54
*/
@Service
public class QuestionFavourServiceImpl extends ServiceImpl<QuestionFavourMapper, QuestionFavour>
    implements QuestionFavourService{
    @Resource
    private QuestionService questionService;

    /**
     * 帖子收藏
     *
     * @param questionId
     * @param loginUser
     * @return
     */
    @Override
    public int doQuestionFavour(long questionId, User loginUser) {
        // 判断是否存在
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已帖子收藏
        long userId = loginUser.getId();
        // 每个用户串行帖子收藏
        // 锁必须要包裹住事务方法
        QuestionFavourService questionFavourService = (QuestionFavourService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return questionFavourService.doQuestionFavourInner(userId, questionId);
        }
    }

    @Override
    public Page<Question> listFavourQuestionByPage(IPage<Question> page, Wrapper<Question> queryWrapper, long favourUserId) {
        if (favourUserId <= 0) {
            return new Page<>();
        }
        return baseMapper.listFavourQuestionByPage(page, queryWrapper, favourUserId);
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param questionId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionFavourInner(long userId, long questionId) {
        QuestionFavour questionFavour = new QuestionFavour();
        questionFavour.setUserId(userId);
        questionFavour.setQuestionId(questionId);
        QueryWrapper<QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>(questionFavour);
        QuestionFavour oldQuestionFavour = this.getOne(questionFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldQuestionFavour != null) {
            result = this.remove(questionFavourQueryWrapper);
            if (result) {
                // 帖子收藏数 - 1
                result = questionService.update()
                        .eq("id", questionId)
                        .gt("favourNum", 0)
                        .setSql("favourNum = favourNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未帖子收藏
            result = this.save(questionFavour);
            if (result) {
                // 帖子收藏数 + 1
                result = questionService.update()
                        .eq("id", questionId)
                        .setSql("favourNum = favourNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}




