package com.vv.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.oj.common.ErrorCode;
import com.vv.oj.constant.CommonConstant;
import com.vv.oj.exception.BusinessException;
import com.vv.oj.exception.ThrowUtils;
import com.vv.oj.model.dto.contestquestion.ContestQuestionQueryRequest;
import com.vv.oj.model.entity.*;
import com.vv.oj.model.vo.ContestQuestionVO;
import com.vv.oj.model.vo.UserVO;
import com.vv.oj.service.ContestQuestionService;
import com.vv.oj.mapper.ContestQuestionMapper;
import com.vv.oj.service.UserService;
import com.vv.oj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author zyz19
* @description 针对表【contest_contestQuestion(题目)】的数据库操作Service实现
* @createDate 2023-08-21 19:15:52
*/
@Service
public class ContestQuestionServiceImpl extends ServiceImpl<ContestQuestionMapper, ContestQuestion>
    implements ContestQuestionService{
    @Resource
    private UserService userService;

    /**
     * 校验题目是否合法
     * @param contestQuestion
     * @param add
     */
    @Override
    public void validContestQuestion(ContestQuestion contestQuestion, boolean add) {
        if (contestQuestion == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = contestQuestion.getTitle();
        String content = contestQuestion.getContent();
        String tags = contestQuestion.getTags();
        String answer = contestQuestion.getAnswer();
        String judgeCase = contestQuestion.getJudgeCase();
        String judgeConfig = contestQuestion.getJudgeConfig();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param contestQuestionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<ContestQuestion> getQueryWrapper(ContestQuestionQueryRequest contestQuestionQueryRequest) {
        QueryWrapper<ContestQuestion> queryWrapper = new QueryWrapper<>();
        if (contestQuestionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = contestQuestionQueryRequest.getId();
        String title = contestQuestionQueryRequest.getTitle();
        String content = contestQuestionQueryRequest.getContent();
        List<String> tags = contestQuestionQueryRequest.getTags();
        Long userId = contestQuestionQueryRequest.getUserId();
        String sortField = contestQuestionQueryRequest.getSortField();
        String sortOrder = contestQuestionQueryRequest.getSortOrder();
        Integer rate = contestQuestionQueryRequest.getRate();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(rate), "rate", rate);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public ContestQuestionVO getContestQuestionVO(ContestQuestion contestQuestion, HttpServletRequest request) {
        ContestQuestionVO contestQuestionVO = ContestQuestionVO.objToVo(contestQuestion);
        // 1. 关联查询用户信息
        Long userId = contestQuestion.getUserId();
        Long contestQuestionId = contestQuestion.getId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        contestQuestionVO.setUserVO(userVO);
        return contestQuestionVO;
    }

    @Override
    public Page<ContestQuestionVO> getContestQuestionVOPage(Page<ContestQuestion> contestQuestionPage, HttpServletRequest request) {
        List<ContestQuestion> contestQuestionList = contestQuestionPage.getRecords();
        Page<ContestQuestionVO> contestQuestionVOPage = new Page<>(contestQuestionPage.getCurrent(), contestQuestionPage.getSize(), contestQuestionPage.getTotal());
        if (CollectionUtils.isEmpty(contestQuestionList)) {
            return contestQuestionVOPage;
        }
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> contestQuestionIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> contestQuestionIdHasFavourMap = new HashMap<>();

        // 1. 关联查询用户信息
        Set<Long> userIdSet = contestQuestionList.stream().map(ContestQuestion::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        List<ContestQuestionVO> contestQuestionVOList = contestQuestionList.stream().map(contestQuestion -> {
            ContestQuestionVO contestQuestionVO = ContestQuestionVO.objToVo(contestQuestion);
            Long userId = contestQuestion.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            contestQuestionVO.setUserVO(userService.getUserVO(user));
            return contestQuestionVO;
        }).collect(Collectors.toList());
        contestQuestionVOPage.setRecords(contestQuestionVOList);
        return contestQuestionVOPage;
    }
}




