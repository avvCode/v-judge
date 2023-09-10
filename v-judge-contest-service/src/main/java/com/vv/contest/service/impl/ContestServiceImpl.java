package com.vv.contest.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.oj.common.ErrorCode;
import com.vv.oj.constant.CommonConstant;
import com.vv.oj.exception.BusinessException;
import com.vv.oj.exception.ThrowUtils;
import com.vv.oj.model.dto.contest.ContestQueryRequest;
import com.vv.oj.model.entity.*;
import com.vv.oj.model.enums.ContestRuleEnum;
import com.vv.oj.model.enums.ContestTypeEnum;
import com.vv.oj.model.vo.ContestQuestionSubmitVO;
import com.vv.oj.model.vo.ContestRankingVO;
import com.vv.oj.model.vo.ContestVO;
import com.vv.oj.model.vo.UserVO;
import com.vv.oj.service.ContestQuestionSubmitService;
import com.vv.oj.service.ContestService;
import com.vv.oj.mapper.ContestMapper;
import com.vv.oj.service.UserService;
import com.vv.oj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author vv
* @description 针对表【contest(赛事表)】的数据库操作Service实现
* @createDate 2023-08-18 16:54:18
*/
@Service
public class ContestServiceImpl extends ServiceImpl<ContestMapper, Contest>
    implements ContestService{
    @Resource
    private UserService userService;
    
    @Override
    public void validContest(Contest contest, boolean add) {
        if (contest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = contest.getTitle();
        String description = contest.getDescription();
        String startTime = contest.getStartTime();
        String endTime = contest.getEndTime();
        Integer rules = contest.getRules();
        Integer type = contest.getType();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(description) && description.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }
        if(rules != ContestRuleEnum.ACM.getCode() && rules != ContestRuleEnum.OI.getCode()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "错误的规则类型");
        }
        if(type != ContestTypeEnum.PRIVATE.getCode() && type != ContestTypeEnum.PUBLIC.getCode()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "错误的比赛类型");
        }

        //获取输入的开始时间
        DateTime star = DateUtil.parse(startTime);
        DateTime end = DateUtil.parse(endTime);
        long between = DateUtil.between(star, DateUtil.date(), DateUnit.HOUR);
        //开启时间不得小于当前时间1h
        if(between < 1){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "比赛开始时间必须大于当前1H");
        }
        long between1 = DateUtil.between(star, end, DateUnit.HOUR);
        //开始时间必须大于当前时间
        if(between1 < 1){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "比赛开始时间必须大于结束比赛1H");
        }

    }

    /**
     * 获取查询包装类
     *
     * @param contestQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Contest> getQueryWrapper(ContestQueryRequest contestQueryRequest) {
        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        if (contestQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = contestQueryRequest.getSortField();
        String sortOrder = contestQueryRequest.getSortOrder();
        Long id = contestQueryRequest.getId();
        String title = contestQueryRequest.getTitle();
        Long userId = contestQueryRequest.getUserId();
        Integer rules = contestQueryRequest.getRules();
        Integer type = contestQueryRequest.getType();
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(rules), "rules", rules);
        queryWrapper.eq(ObjectUtils.isNotEmpty(type), "type", type);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public ContestVO getContestVO(Contest contest, HttpServletRequest request) {
        ContestVO contestVO = ContestVO.objToVo(contest);
        // 1. 关联查询用户信息
        Long userId = contest.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        contestVO.setUserVO(userVO);
        return contestVO;
    }

    @Override
    public Page<ContestVO> getContestVOPage(Page<Contest> contestPage, HttpServletRequest request) {
        List<Contest> contestList = contestPage.getRecords();
        Page<ContestVO> contestVOPage = new Page<>(contestPage.getCurrent(), contestPage.getSize(), contestPage.getTotal());
        if (CollectionUtils.isEmpty(contestList)) {
            return contestVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = contestList.stream().map(Contest::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        List<ContestVO> contestVOList = contestList
                .stream()
                .map(contest -> {
                    ContestVO contestVO = ContestVO.objToVo(contest);
                    Long userId = contest.getUserId();
                    User user = null;
                    if (userIdUserListMap.containsKey(userId)) {
                        user = userIdUserListMap.get(userId).get(0);
                    }
                    contestVO.setUserVO(userService.getUserVO(user));
                    return contestVO;
                })
                .collect(Collectors.toList());
        contestVOPage.setRecords(contestVOList);
        return contestVOPage;
    }


}




