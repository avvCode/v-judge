package com.vv.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.oj.model.dto.contestquestionsubmit.ContestQuestionSubmitAddRequest;
import com.vv.oj.model.dto.contestquestionsubmit.ContestQuestionSubmitQueryRequest;
import com.vv.oj.model.dto.contestquestionsubmit.ContestRankingQueryRequest;
import com.vv.oj.model.entity.ContestQuestionSubmit;
import com.vv.oj.model.entity.User;
import com.vv.oj.model.vo.ContestQuestionSubmitVO;
import com.vv.oj.model.vo.ContestRankingVO;

/**
 * @author zyz19
 * @description 针对表【contest_question_submit(赛事题目提交表)】的数据库操作Service
 * @createDate 2023-08-25 19:55:00
 */
public interface ContestQuestionSubmitService extends IService<ContestQuestionSubmit> {
    /**
     * 题目提交
     *
     * @param contestQuestionSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return
     */
    long doContestQuestionSubmit(ContestQuestionSubmitAddRequest contestQuestionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param contestContestQuestionSubmitQueryRequest
     * @return
     */
    QueryWrapper<ContestQuestionSubmit> getQueryWrapper(ContestQuestionSubmitQueryRequest contestContestQuestionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param contestContestQuestionSubmit
     * @param loginUser
     * @return
     */
    ContestQuestionSubmitVO getContestQuestionSubmitVO(ContestQuestionSubmit contestContestQuestionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param contestContestQuestionSubmitPage
     * @param loginUser
     * @return
     */
    Page<ContestQuestionSubmitVO> getContestQuestionSubmitVOPage(Page<ContestQuestionSubmit> contestContestQuestionSubmitPage, User loginUser);


    Page<ContestRankingVO> getContestRankingVO(ContestRankingQueryRequest contestRankingQueryRequest);
}
