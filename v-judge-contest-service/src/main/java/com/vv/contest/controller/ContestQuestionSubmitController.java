package com.vv.contest.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vv.common.common.BaseResponse;
import com.vv.common.common.ErrorCode;
import com.vv.common.common.ResultUtils;
import com.vv.common.exception.BusinessException;
import com.vv.contest.service.ContestQuestionSubmitService;
import com.vv.model.dto.contestquestionsubmit.ContestQuestionSubmitAddRequest;
import com.vv.model.dto.contestquestionsubmit.ContestQuestionSubmitQueryRequest;
import com.vv.model.dto.contestquestionsubmit.ContestRankingQueryRequest;
import com.vv.model.entity.ContestQuestionSubmit;
import com.vv.model.entity.User;
import com.vv.model.vo.ContestQuestionSubmitVO;
import com.vv.model.vo.ContestRankingVO;
import com.vv.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author vv
 */
@RestController
@RequestMapping("/contest_question_submit")
@Slf4j
public class ContestQuestionSubmitController {
    @Resource
    private ContestQuestionSubmitService contestQuestionSubmitService;


    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 提交题目
     *
     * @param contestQuestionSubmitAddRequest
     * @param request
     * @return 提交记录的 id
     */
    @PostMapping("/")
    public BaseResponse<Long> doContestQuestionSubmit(@RequestBody ContestQuestionSubmitAddRequest contestQuestionSubmitAddRequest,
                                                      HttpServletRequest request) {
        if (contestQuestionSubmitAddRequest == null || contestQuestionSubmitAddRequest.getContestQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        final User loginUser = userFeignClient.getLoginUser(request);
        long contestQuestionSubmitId = contestQuestionSubmitService.doContestQuestionSubmit(contestQuestionSubmitAddRequest, loginUser);
        return ResultUtils.success(contestQuestionSubmitId);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param contestQuestionSubmitQueryRequest
     * @param request
     * @return
     *
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ContestQuestionSubmitVO>> listContestQuestionSubmitByPage(@RequestBody ContestQuestionSubmitQueryRequest contestQuestionSubmitQueryRequest,
                                                                                       HttpServletRequest request) {
        long current = contestQuestionSubmitQueryRequest.getCurrent();
        long size = contestQuestionSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<ContestQuestionSubmit> contestQuestionSubmitPage = contestQuestionSubmitService.page(new Page<>(current, size),
                contestQuestionSubmitService.getQueryWrapper(contestQuestionSubmitQueryRequest));
        final User loginUser = userFeignClient.getLoginUser(request);
        // 返回脱敏信息
        return ResultUtils.success(contestQuestionSubmitService.getContestQuestionSubmitVOPage(contestQuestionSubmitPage, loginUser));
    }

    /**
     * 根据Id获取题目提交（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param id
     * @return
     */
    @PostMapping("/get")
    public BaseResponse<ContestQuestionSubmitVO> getContestQuestionSubmitById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ContestQuestionSubmit contestQuestionSubmit = contestQuestionSubmitService.getById(id);
        if (contestQuestionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        final User loginUser = userFeignClient.getLoginUser(request);
        return ResultUtils.success(contestQuestionSubmitService.getContestQuestionSubmitVO(contestQuestionSubmit, loginUser));
    }
    @PostMapping("/ranking")
    public BaseResponse<Page<ContestRankingVO>> getContestRanking(ContestRankingQueryRequest contestRankingQueryRequest){
        if(contestRankingQueryRequest == null || contestRankingQueryRequest.getContestId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<ContestRankingVO> rankingVOPage = contestQuestionSubmitService.getContestRankingVO(contestRankingQueryRequest);
        return ResultUtils.success(rankingVOPage);
    }
}
