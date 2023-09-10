package com.vv.question.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vv.common.common.BaseResponse;
import com.vv.common.common.ErrorCode;
import com.vv.common.common.ResultUtils;
import com.vv.common.exception.BusinessException;
import com.vv.common.exception.ThrowUtils;
import com.vv.model.dto.question.QuestionQueryRequest;
import com.vv.model.dto.questionfavour.QuestionFavourAddRequest;
import com.vv.model.dto.questionfavour.QuestionFavourQueryRequest;
import com.vv.model.entity.Question;
import com.vv.model.entity.User;
import com.vv.model.vo.QuestionVO;
import com.vv.question.service.QuestionFavourService;
import com.vv.question.service.QuestionService;
import com.vv.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目收藏接口
 *
 * @author vv
 */
@RestController
@RequestMapping("/question_favour")
@Slf4j
public class QuestionFavourController {

    @Resource
    private QuestionFavourService questionFavourService;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userFeignClient;

    /**
     * 收藏 / 取消收藏
     *
     * @param questionFavourAddRequest
     * @param request
     * @return resultNum 收藏变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doQuestionFavour(@RequestBody QuestionFavourAddRequest questionFavourAddRequest,
                                                  HttpServletRequest request) {
        if (questionFavourAddRequest == null || questionFavourAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userFeignClient.getLoginUser(request);
        long questionId = questionFavourAddRequest.getQuestionId();
        int result = questionFavourService.doQuestionFavour(questionId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我收藏的题目列表
     *
     * @param questionQueryRequest
     * @param request
     */
    @PostMapping("/my/list/page")
    public BaseResponse<Page<QuestionVO>> listMyFavourQuestionByPage(@RequestBody QuestionQueryRequest questionQueryRequest,
                                                                     HttpServletRequest request) {
        if (questionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        long current = questionQueryRequest.getCurrent();
        long size = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionFavourService.listFavourQuestionByPage(new Page<>(current, size),
                questionService.getQueryWrapper(questionQueryRequest), loginUser.getId());
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }

    /**
     * 获取用户收藏的题目列表
     *
     * @param questionFavourQueryRequest
     * @param request
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionVO>> listFavourQuestionByPage(@RequestBody QuestionFavourQueryRequest questionFavourQueryRequest,
            HttpServletRequest request) {
        if (questionFavourQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = questionFavourQueryRequest.getCurrent();
        long size = questionFavourQueryRequest.getPageSize();
        Long userId = questionFavourQueryRequest.getUserId();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20 || userId == null, ErrorCode.PARAMS_ERROR);
        Page<Question> questionPage = questionFavourService.listFavourQuestionByPage(new Page<>(current, size),
                questionService.getQueryWrapper(questionFavourQueryRequest.getQuestionQueryRequest()), userId);
        return ResultUtils.success(questionService.getQuestionVOPage(questionPage, request));
    }
}
