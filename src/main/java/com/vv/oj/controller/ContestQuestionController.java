package com.vv.oj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.vv.oj.annotation.AuthCheck;
import com.vv.oj.common.BaseResponse;
import com.vv.oj.common.DeleteRequest;
import com.vv.oj.common.ErrorCode;
import com.vv.oj.common.ResultUtils;
import com.vv.oj.constant.UserConstant;
import com.vv.oj.exception.BusinessException;
import com.vv.oj.exception.ThrowUtils;
import com.vv.oj.model.dto.contestquestion.ContestQuestionAddRequest;
import com.vv.oj.model.dto.contestquestion.ContestQuestionEditRequest;
import com.vv.oj.model.dto.contestquestion.ContestQuestionQueryRequest;
import com.vv.oj.model.dto.contestquestion.ContestQuestionUpdateRequest;
import com.vv.oj.model.dto.question.JudgeCase;
import com.vv.oj.model.dto.question.JudgeConfig;
import com.vv.oj.model.entity.ContestQuestion;
import com.vv.oj.model.entity.User;
import com.vv.oj.model.vo.ContestQuestionVO;
import com.vv.oj.service.ContestQuestionService;
import com.vv.oj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题目接口
 *
 * @author vv
 */
@RestController
@RequestMapping("/contest_question")
@Slf4j
public class ContestQuestionController {

    @Resource
    private ContestQuestionService contestQuestionService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    // region 增删改查
    /**
     * 创建
     *
     * @param contestQuestionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addContestQuestion(@RequestBody ContestQuestionAddRequest contestQuestionAddRequest, HttpServletRequest request) {
        if (contestQuestionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ContestQuestion contestQuestion = new ContestQuestion();
        BeanUtils.copyProperties(contestQuestionAddRequest, contestQuestion);
        List<String> tags = contestQuestionAddRequest.getTags();
        if (tags != null) {
            contestQuestion.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = contestQuestionAddRequest.getJudgeCase();
        if (judgeCase != null) {
            contestQuestion.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = contestQuestionAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            contestQuestion.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        Long contestId = contestQuestion.getContestId();
        if(contestId == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        contestQuestionService.validContestQuestion(contestQuestion, true);
        User loginUser = userService.getLoginUser(request);
        contestQuestion.setUserId(loginUser.getId());
        contestQuestion.setContestId(contestId);
        boolean result = contestQuestionService.save(contestQuestion);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newContestQuestionId = contestQuestion.getId();
        return ResultUtils.success(newContestQuestionId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteContestQuestion(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        ContestQuestion oldContestQuestion = contestQuestionService.getById(id);
        ThrowUtils.throwIf(oldContestQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldContestQuestion.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = contestQuestionService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param contestQuestionUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateContestQuestion(@RequestBody ContestQuestionUpdateRequest contestQuestionUpdateRequest) {
        if (contestQuestionUpdateRequest == null || contestQuestionUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ContestQuestion contestQuestion = new ContestQuestion();
        BeanUtils.copyProperties(contestQuestionUpdateRequest, contestQuestion);
        List<String> tags = contestQuestionUpdateRequest.getTags();
        if (tags != null) {
            contestQuestion.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = contestQuestionUpdateRequest.getJudgeCase();
        if (judgeCase != null) {
            contestQuestion.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = contestQuestionUpdateRequest.getJudgeConfig();
        if (judgeConfig != null) {
            contestQuestion.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        // 参数校验
        contestQuestionService.validContestQuestion(contestQuestion, false);
        long id = contestQuestionUpdateRequest.getId();
        // 判断是否存在
        ContestQuestion oldContestQuestion = contestQuestionService.getById(id);
        ThrowUtils.throwIf(oldContestQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = contestQuestionService.updateById(contestQuestion);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<ContestQuestionVO> getContestQuestionVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ContestQuestion contestQuestion = contestQuestionService.getById(id);
        if (contestQuestion == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        //不是本人或者管理源。不能直接获取所有信息
        if(!contestQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(contestQuestionService.getContestQuestionVO(contestQuestion, request));
    }
    /**
     * 根据 id 获取 （脱敏）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<ContestQuestion> getContestQuestionById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ContestQuestion contestQuestion = contestQuestionService.getById(id);
        if (contestQuestion == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(contestQuestion);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param contestQuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ContestQuestionVO>> listContestQuestionVOByPage(@RequestBody ContestQuestionQueryRequest contestQuestionQueryRequest,
                                                               HttpServletRequest request) {
        long current = contestQuestionQueryRequest.getCurrent();
        long size = contestQuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ContestQuestion> contestQuestionPage = contestQuestionService.page(new Page<>(current, size),
                contestQuestionService.getQueryWrapper(contestQuestionQueryRequest));
        return ResultUtils.success(contestQuestionService.getContestQuestionVOPage(contestQuestionPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param contestQuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<ContestQuestionVO>> listMyContestQuestionVOByPage(@RequestBody ContestQuestionQueryRequest contestQuestionQueryRequest,
                                                                 HttpServletRequest request) {
        if (contestQuestionQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        contestQuestionQueryRequest.setUserId(loginUser.getId());
        long current = contestQuestionQueryRequest.getCurrent();
        long size = contestQuestionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ContestQuestion> contestQuestionPage = contestQuestionService.page(new Page<>(current, size),
                contestQuestionService.getQueryWrapper(contestQuestionQueryRequest));
        return ResultUtils.success(contestQuestionService.getContestQuestionVOPage(contestQuestionPage, request));
    }

    /**
     * 分页获取题目列表（仅管理员）
     *
     * @param contestQuestionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<ContestQuestion>> listContestQuestionByPage(@RequestBody ContestQuestionQueryRequest contestQuestionQueryRequest,
                                                           HttpServletRequest request) {
        long current = contestQuestionQueryRequest.getCurrent();
        long size = contestQuestionQueryRequest.getPageSize();
        Page<ContestQuestion> contestQuestionPage = contestQuestionService.page(new Page<>(current, size),
                contestQuestionService.getQueryWrapper(contestQuestionQueryRequest));
        return ResultUtils.success(contestQuestionPage);
    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param contestQuestionEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editContestQuestion(@RequestBody ContestQuestionEditRequest contestQuestionEditRequest, HttpServletRequest request) {
        if (contestQuestionEditRequest == null || contestQuestionEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ContestQuestion contestQuestion = new ContestQuestion();
        BeanUtils.copyProperties(contestQuestionEditRequest, contestQuestion);
        List<String> tags = contestQuestionEditRequest.getTags();
        if (tags != null) {
            contestQuestion.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = contestQuestionEditRequest.getJudgeCase();
        if (judgeCase != null) {
            contestQuestion.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = contestQuestionEditRequest.getJudgeConfig();
        if (judgeConfig != null) {
            contestQuestion.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        // 参数校验
        contestQuestionService.validContestQuestion(contestQuestion, false);
        User loginUser = userService.getLoginUser(request);
        long id = contestQuestionEditRequest.getId();
        // 判断是否存在
        ContestQuestion oldContestQuestion = contestQuestionService.getById(id);
        ThrowUtils.throwIf(oldContestQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldContestQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = contestQuestionService.updateById(contestQuestion);
        return ResultUtils.success(result);
    }

}
