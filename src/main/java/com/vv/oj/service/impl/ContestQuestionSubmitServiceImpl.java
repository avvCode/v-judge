package com.vv.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.oj.common.ErrorCode;
import com.vv.oj.constant.CommonConstant;
import com.vv.oj.exception.BusinessException;
import com.vv.oj.mapper.ContestQuestionSubmitMapper;
import com.vv.oj.model.dto.contestquestionsubmit.ContestQuestionSubmitAddRequest;
import com.vv.oj.model.dto.contestquestionsubmit.ContestQuestionSubmitQueryRequest;
import com.vv.oj.model.dto.contestquestionsubmit.ContestQuestionSubmitAddRequest;
import com.vv.oj.model.dto.contestquestionsubmit.ContestQuestionSubmitQueryRequest;
import com.vv.oj.model.entity.ContestQuestionSubmit;
import com.vv.oj.model.entity.ContestQuestion;
import com.vv.oj.model.entity.ContestQuestionSubmit;
import com.vv.oj.model.entity.User;
import com.vv.oj.model.enums.ContestQuestionSubmitLanguageEnum;
import com.vv.oj.model.enums.ContestQuestionSubmitStatusEnum;
import com.vv.oj.model.vo.ContestQuestionSubmitVO;
import com.vv.oj.model.vo.ContestQuestionVO;
import com.vv.oj.model.vo.UserVO;
import com.vv.oj.service.ContestQuestionSubmitService;
import com.vv.oj.service.ContestQuestionService;
import com.vv.oj.service.UserService;
import com.vv.oj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author zyz19
* @description 针对表【contest_contestQuestion_submit(赛事题目提交表)】的数据库操作Service实现
* @createDate 2023-08-25 19:55:00
*/
@Service
public class ContestQuestionSubmitServiceImpl extends ServiceImpl<ContestQuestionSubmitMapper, ContestQuestionSubmit>
    implements ContestQuestionSubmitService{
    @Resource
    private ContestQuestionService contestQuestionService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param contestQuestionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doContestQuestionSubmit(ContestQuestionSubmitAddRequest contestQuestionSubmitAddRequest, User loginUser) {
        // 校验编程语言是否合法
        String language = contestQuestionSubmitAddRequest.getLanguage();
        ContestQuestionSubmitLanguageEnum languageEnum = ContestQuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        long contestQuestionId = contestQuestionSubmitAddRequest.getContestQuestionId();
        // 判断实体是否存在，根据类别获取实体
        ContestQuestion contestQuestion = contestQuestionService.getById(contestQuestionId);
        if (contestQuestion == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        ContestQuestionSubmit contestQuestionSubmit = new ContestQuestionSubmit();
        contestQuestionSubmit.setUserId(userId);
        contestQuestionSubmit.setContestQuestionId(contestQuestionId);
        contestQuestionSubmit.setCode(contestQuestionSubmitAddRequest.getCode());
        contestQuestionSubmit.setLanguage(language);
        // 设置初始状态
        contestQuestionSubmit.setStatus(ContestQuestionSubmitStatusEnum.WAITING.getValue());
        contestQuestionSubmit.setJudgeInfo("{}");
        boolean save = this.save(contestQuestionSubmit);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        return contestQuestionSubmit.getId();
    }


    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询 QueryWrapper 类）
     *
     * @param contestQuestionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<ContestQuestionSubmit> getQueryWrapper(ContestQuestionSubmitQueryRequest contestQuestionSubmitQueryRequest) {
        QueryWrapper<ContestQuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (contestQuestionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = contestQuestionSubmitQueryRequest.getLanguage();
        Integer status = contestQuestionSubmitQueryRequest.getStatus();
        Long contestQuestionId = contestQuestionSubmitQueryRequest.getContestQuestionId();
        Long userId = contestQuestionSubmitQueryRequest.getUserId();
        Long contestId = contestQuestionSubmitQueryRequest.getContestId();
        String sortField = contestQuestionSubmitQueryRequest.getSortField();
        String sortOrder = contestQuestionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        if(contestQuestionId != null && contestQuestionId > 0){
            queryWrapper.eq(ObjectUtils.isNotEmpty(contestQuestionId), "contestQuestionId", contestQuestionId);
        }
        if(contestId != null && contestId > 0){
            queryWrapper.eq(ObjectUtils.isNotEmpty(contestId), "contestId", contestId);
        }
        queryWrapper.eq(ContestQuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public ContestQuestionSubmitVO getContestQuestionSubmitVO(ContestQuestionSubmit contestQuestionSubmit, User loginUser) {
        ContestQuestionSubmitVO contestQuestionSubmitVO = ContestQuestionSubmitVO.objToVo(contestQuestionSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        Long createUserId = contestQuestionSubmit.getUserId();
        if (userId != createUserId && !userService.isAdmin(loginUser)) {
            contestQuestionSubmitVO.setCode(null);
        }
        //获取创建人VO
        UserVO userVO = userService.getUserVOById(createUserId);
        contestQuestionSubmitVO.setUserVO(userVO);
        //获取题目VO
        ContestQuestion contestQuestion = contestQuestionService.getById(contestQuestionSubmit.getContestQuestionId());
        ContestQuestionVO contestQuestionVO = ContestQuestionVO.objToVo(contestQuestion);
        contestQuestionSubmitVO.setContestQuestionVO(contestQuestionVO);
        return contestQuestionSubmitVO;
    }

    @Override
    public Page<ContestQuestionSubmitVO> getContestQuestionSubmitVOPage(Page<ContestQuestionSubmit> contestQuestionSubmitPage, User loginUser) {
        List<ContestQuestionSubmit> contestQuestionSubmitList = contestQuestionSubmitPage.getRecords();
        Page<ContestQuestionSubmitVO> contestQuestionSubmitVOPage = new Page<>(contestQuestionSubmitPage.getCurrent(), contestQuestionSubmitPage.getSize(), contestQuestionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(contestQuestionSubmitList)) {
            return contestQuestionSubmitVOPage;
        }
        List<ContestQuestionSubmitVO> contestQuestionSubmitVOList = contestQuestionSubmitList.stream()
                .map(contestQuestionSubmit -> getContestQuestionSubmitVO(contestQuestionSubmit, loginUser))
                .collect(Collectors.toList());
        contestQuestionSubmitVOPage.setRecords(contestQuestionSubmitVOList);
        return contestQuestionSubmitVOPage;
    }
}




