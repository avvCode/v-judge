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
import com.vv.contest.service.UserContestService;
import com.vv.model.dto.usercontest.UserContestAddRequest;
import com.vv.model.dto.usercontest.UserContestQueryRequest;
import com.vv.model.dto.usercontest.UserContestUpdateRequest;
import com.vv.model.entity.User;
import com.vv.model.entity.UserContest;
import com.vv.model.vo.UserContestVO;
import com.vv.service.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author vv
 */
@RestController
@RequestMapping("/user_contest")
@Slf4j
public class UserContestController {
    @Resource
    private UserContestService userContestService;

    @Resource
    private UserFeignClient userFeignClient;


    // region 增删改查

    /**
     * 创建
     *
     * @param userContestAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addUserContest(@RequestBody UserContestAddRequest userContestAddRequest, HttpServletRequest request) {
        if (userContestAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserContest userContest = new UserContest();
        BeanUtils.copyProperties(userContestAddRequest, userContest);
        userContestService.validUserContest(userContest, true);
        Long contestId = userContestAddRequest.getContestId();
        Long userId = userContestAddRequest.getUserId();
        userContest.setUserId(userId);
        userContest.setContestId(contestId);
        boolean result = userContestService.save(userContest);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(null);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserContest(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userFeignClient.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        UserContest oldUserContest = userContestService.getById(id);
        ThrowUtils.throwIf(oldUserContest == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldUserContest.getUserId().equals(user.getId()) && !userFeignClient.isAdmin(user)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userContestService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param userContestUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserContest(@RequestBody UserContestUpdateRequest userContestUpdateRequest) {
        if (userContestUpdateRequest == null || userContestUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserContest userContest = new UserContest();
        BeanUtils.copyProperties(userContestUpdateRequest, userContest);
        // 参数校验
        userContestService.validUserContest(userContest, false);
        long id = userContestUpdateRequest.getId();
        // 判断是否存在
        UserContest oldUserContest = userContestService.getById(id);
        ThrowUtils.throwIf(oldUserContest == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = userContestService.updateById(userContest);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserContestVO> getUserContestVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserContest userContest = userContestService.getById(id);
        if (userContest == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(userContestService.getUserContestVO(userContest, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param userContestQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserContestVO>> listUserContestVOByPage(@RequestBody UserContestQueryRequest userContestQueryRequest,
                                                       HttpServletRequest request) {
        long current = userContestQueryRequest.getCurrent();
        long size = userContestQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<UserContest> userContestPage = userContestService.page(new Page<>(current, size),
                userContestService.getQueryWrapper(userContestQueryRequest));
        return ResultUtils.success(userContestService.getUserContestVOPage(userContestPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param userContestQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<UserContestVO>> listMyUserContestVOByPage(@RequestBody UserContestQueryRequest userContestQueryRequest,
                                                         HttpServletRequest request) {
        if (userContestQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userFeignClient.getLoginUser(request);
        userContestQueryRequest.setUserId(loginUser.getId());
        long current = userContestQueryRequest.getCurrent();
        long size = userContestQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<UserContest> userContestPage = userContestService.page(new Page<>(current, size),
                userContestService.getQueryWrapper(userContestQueryRequest));
        return ResultUtils.success(userContestService.getUserContestVOPage(userContestPage, request));
    }

    // endregion
}
