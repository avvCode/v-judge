package com.vv.contest.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.model.dto.usercontest.UserContestQueryRequest;
import com.vv.model.entity.UserContest;
import com.vv.model.vo.UserContestVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author vv
* @description 针对表【user_contest(用户参赛记录表)】的数据库操作Service
* @createDate 2023-08-18 16:54:18
*/
public interface UserContestService extends IService<UserContest> {
    /**
     * 校验
     *
     * @param userContest
     * @param add
     */
    void validUserContest(UserContest userContest, boolean add);

    /**
     * 获取查询条件
     *
     * @param userContestQueryRequest
     * @return
     */
    QueryWrapper<UserContest> getQueryWrapper(UserContestQueryRequest userContestQueryRequest);


    /**
     * 获取参赛记录封装
     *
     * @param userContest
     * @param request
     * @return
     */
    UserContestVO getUserContestVO(UserContest userContest, HttpServletRequest request);

    /**
     * 分页获取参赛记录封装
     *
     * @param userContestPage
     * @param request
     * @return
     */
    Page<UserContestVO> getUserContestVOPage(Page<UserContest> userContestPage, HttpServletRequest request);
}
