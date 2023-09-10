package com.vv.contest.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vv.common.annotation.AuthCheck;
import com.vv.common.common.BaseResponse;
import com.vv.common.common.DeleteRequest;
import com.vv.common.common.ErrorCode;
import com.vv.common.common.ResultUtils;
import com.vv.common.constant.UserConstant;
import com.vv.common.exception.BusinessException;
import com.vv.common.exception.ThrowUtils;
import com.vv.contest.service.ContestService;
import com.vv.model.dto.contest.ContestAddRequest;
import com.vv.model.dto.contest.ContestEditRequest;
import com.vv.model.dto.contest.ContestQueryRequest;
import com.vv.model.dto.contest.ContestUpdateRequest;
import com.vv.model.entity.Contest;
import com.vv.model.entity.User;
import com.vv.model.vo.ContestVO;
import com.vv.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 赛事接口
 *
 * @author vv
 */
@RestController
@RequestMapping("/question_contest")
@Slf4j
public class ContestController {

    @Resource
    private ContestService contestService;

    private UserFeignClient userFeignClient;

    // region 增删改查

    /**
     * 创建
     *
     * @param contestAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addContest(@RequestBody ContestAddRequest contestAddRequest, HttpServletRequest request) {
        if (contestAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Contest contest = new Contest();
        BeanUtils.copyProperties(contestAddRequest, contest);
        contestService.validContest(contest, true);
        User loginUser = userFeignClient.getLoginUser(request);
        contest.setUserId(loginUser.getId());
        boolean result = contestService.save(contest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newContestId = contest.getId();
        return ResultUtils.success(newContestId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteContest(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Contest oldContest = contestService.getById(id);
        ThrowUtils.throwIf(oldContest == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldContest.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = contestService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param contestUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateContest(@RequestBody ContestUpdateRequest contestUpdateRequest) {
        if (contestUpdateRequest == null || contestUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Contest contest = new Contest();
        BeanUtils.copyProperties(contestUpdateRequest, contest);
        // 参数校验
        contestService.validContest(contest, false);
        long id = contestUpdateRequest.getId();
        // 判断是否存在
        Contest oldContest = contestService.getById(id);
        ThrowUtils.throwIf(oldContest == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = contestService.updateById(contest);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<ContestVO> getContestVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Contest contest = contestService.getById(id);
        if (contest == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(contestService.getContestVO(contest, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param contestQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ContestVO>> listContestVOByPage(@RequestBody ContestQueryRequest contestQueryRequest,
                                                       HttpServletRequest request) {
        long current = contestQueryRequest.getCurrent();
        long size = contestQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Contest> contestPage = contestService.page(new Page<>(current, size),
                contestService.getQueryWrapper(contestQueryRequest));
        return ResultUtils.success(contestService.getContestVOPage(contestPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param contestQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<ContestVO>> listMyContestVOByPage(@RequestBody ContestQueryRequest contestQueryRequest,
                                                         HttpServletRequest request) {
        if (contestQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        contestQueryRequest.setUserId(loginUser.getId());
        long current = contestQueryRequest.getCurrent();
        long size = contestQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Contest> contestPage = contestService.page(new Page<>(current, size),
                contestService.getQueryWrapper(contestQueryRequest));
        return ResultUtils.success(contestService.getContestVOPage(contestPage, request));
    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param contestEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editContest(@RequestBody ContestEditRequest contestEditRequest, HttpServletRequest request) {
        if (contestEditRequest == null || contestEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Contest contest = new Contest();
        BeanUtils.copyProperties(contestEditRequest, contest);
        // 参数校验
        contestService.validContest(contest, false);
        User loginUser = userFeignClient.getLoginUser(request);
        long id = contestEditRequest.getId();
        // 判断是否存在
        Contest oldContest = contestService.getById(id);
        ThrowUtils.throwIf(oldContest == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldContest.getUserId().equals(loginUser.getId()) && !userFeignClient.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = contestService.updateById(contest);
        return ResultUtils.success(result);
    }


}
