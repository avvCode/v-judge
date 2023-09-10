package com.vv.contest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.common.common.ErrorCode;
import com.vv.common.constant.CommonConstant;
import com.vv.common.exception.BusinessException;
import com.vv.common.utils.SqlUtils;
import com.vv.contest.mapper.UserContestMapper;
import com.vv.contest.service.ContestService;
import com.vv.contest.service.UserContestService;
import com.vv.model.dto.usercontest.UserContestQueryRequest;
import com.vv.model.entity.Contest;
import com.vv.model.entity.User;
import com.vv.model.entity.UserContest;
import com.vv.model.vo.ContestVO;
import com.vv.model.vo.UserContestVO;
import com.vv.model.vo.UserVO;
import com.vv.service.UserFeignClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author vv
 * @description 针对表【user_contest(用户参赛记录表)】的数据库操作Service实现
 * @createDate 2023-08-18 16:54:18
 */
@Service
public class UserContestServiceImpl extends ServiceImpl<UserContestMapper, UserContest>
        implements UserContestService {
    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private ContestService contestService;

    @Override
    public void validUserContest(UserContest userContest, boolean add) {
        if (userContest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = userContest.getUserId();
        Long contestId = userContest.getContestId();
        // 创建时，参数不能为空
        if (add) {
            if (userId == null || contestId == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
    }

    /**
     * 获取查询包装类
     *
     * @param userContestQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<UserContest> getQueryWrapper(UserContestQueryRequest userContestQueryRequest) {
        QueryWrapper<UserContest> queryWrapper = new QueryWrapper<>();
        if (userContestQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = userContestQueryRequest.getSortField();
        String sortOrder = userContestQueryRequest.getSortOrder();
        Long userId = userContestQueryRequest.getUserId();
        Long contestId = userContestQueryRequest.getContestId();
        Integer status = userContestQueryRequest.getStatus();

        // 拼接查询条件
        if (userId != null && userId > 0) {
            queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        }
        if (contestId != null && contestId > 0) {
            queryWrapper.eq(ObjectUtils.isNotEmpty(contestId), "contestId", userId);
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public UserContestVO getUserContestVO(UserContest userContest, HttpServletRequest request) {
        UserContestVO userContestVO = UserContestVO.objToVo(userContest);
        Long contestId = userContest.getContestId();
        // 1. 关联查询用户信息
        Long userId = userContest.getUserId();
        User user = null;
        Contest contest = null;
        if (userId != null && userId > 0) {
            user = userFeignClient.getById(userId);
            UserVO userVO = userFeignClient.getUserVO(user);
            userContestVO.setUserVO(userVO);
        }
        if (contestId!= null && contestId > 0) {
            contest = contestService.getById(contestId);
            ContestVO contestVO = contestService.getContestVO(contest, request);
            userContestVO.setContestVO(contestVO);
        }
        return userContestVO;
    }

    @Override
    public Page<UserContestVO> getUserContestVOPage(Page<UserContest> userContestPage, HttpServletRequest request) {
        List<UserContest> userContestList = userContestPage.getRecords();
        Page<UserContestVO> userContestVOPage = new Page<>(userContestPage.getCurrent(), userContestPage.getSize(), userContestPage.getTotal());
        if (CollectionUtils.isEmpty(userContestList)) {
            return userContestVOPage;
        }
        // 填充信息
        List<UserContestVO> userContestVOList = userContestList.stream().map(userContest -> {
            UserContestVO userContestVO = UserContestVO.objToVo(userContest);
            Long contestId = userContest.getContestId();
            // 1. 关联查询用户信息
            Long userId = userContest.getUserId();
            User user = null;
            Contest contest = null;
            if (userId != null && userId > 0) {
                user = userFeignClient.getById(userId);
                UserVO userVO = userFeignClient.getUserVO(user);
                userContestVO.setUserVO(userVO);
            }
            if (contestId!= null && contestId > 0) {
                contest = contestService.getById(contestId);
                ContestVO contestVO = contestService.getContestVO(contest, request);
                userContestVO.setContestVO(contestVO);
            }
            return userContestVO;
        }).collect(Collectors.toList());
        userContestVOPage.setRecords(userContestVOList);
        return userContestVOPage;
    }
}




