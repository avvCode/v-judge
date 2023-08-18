package com.vv.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vv.oj.model.dto.contest.ContestQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.vv.oj.model.entity.Contest;
import com.vv.oj.model.vo.ContestVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author vv
* @description 针对表【contest(赛事表)】的数据库操作Service
* @createDate 2023-08-18 16:54:18
*/
public interface ContestService extends IService<Contest> {
    /**
     * 校验
     *
     * @param contest
     * @param add
     */
    void validContest(Contest contest, boolean add);

    /**
     * 获取查询条件
     *
     * @param contestQueryRequest
     * @return
     */
    QueryWrapper<Contest> getQueryWrapper(ContestQueryRequest contestQueryRequest);


    /**
     * 获取帖子封装
     *
     * @param contest
     * @param request
     * @return
     */
    ContestVO getContestVO(Contest contest, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param contestPage
     * @param request
     * @return
     */
    Page<ContestVO> getContestVOPage(Page<Contest> contestPage, HttpServletRequest request);

}
