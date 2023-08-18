package com.vv.oj.controller;

import com.vv.oj.common.BaseResponse;
import com.vv.oj.common.ErrorCode;
import com.vv.oj.common.ResultUtils;
import com.vv.oj.exception.BusinessException;
import com.vv.oj.model.dto.questionthumb.QuestionThumbAddRequest;
import com.vv.oj.model.entity.User;
import com.vv.oj.service.QuestionThumbService;
import com.vv.oj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目点赞接口
 *
 * @author vv
 */
@RestController
@RequestMapping("/question_thumb")
@Slf4j
public class QuestionThumbController {

    @Resource
    private QuestionThumbService questionThumbService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param questionThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody QuestionThumbAddRequest questionThumbAddRequest,
            HttpServletRequest request) {
        if (questionThumbAddRequest == null || questionThumbAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long questionId = questionThumbAddRequest.getQuestionId();
        int result = questionThumbService.doQuestionThumb(questionId, loginUser);
        return ResultUtils.success(result);
    }

}
