package com.vv.contest.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vv.oj.model.dto.contestquestion.ContestQuestionQueryRequest;
import com.vv.oj.model.dto.question.QuestionQueryRequest;
import com.vv.oj.model.entity.ContestQuestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.oj.model.entity.Question;
import com.vv.oj.model.vo.ContestQuestionVO;
import com.vv.oj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author zyz19
* @description 针对表【contest_question(题目)】的数据库操作Service
* @createDate 2023-08-21 19:15:52
*/
public interface ContestQuestionService extends IService<ContestQuestion> {
    /**
     * 校验
     *
     * @param contestQuestion
     * @param add
     */
    void validContestQuestion(ContestQuestion contestQuestion, boolean add);

    /**
     * 获取查询条件
     *
     * @param contestQuestionQueryRequest
     * @return
     */
    QueryWrapper<ContestQuestion> getQueryWrapper(ContestQuestionQueryRequest contestQuestionQueryRequest);


    /**
     * 获取题目封装
     *
     * @param contestQuestion
     * @param request
     * @return
     */
    ContestQuestionVO getContestQuestionVO(ContestQuestion contestQuestion, HttpServletRequest request);


    /**
     * 分页获取题目封装
     *
     * @param contestQuestionPage
     * @param request
     * @return
     */
    Page<ContestQuestionVO> getContestQuestionVOPage(Page<ContestQuestion> contestQuestionPage, HttpServletRequest request);
}
