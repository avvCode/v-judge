package com.vv.oj.service.impl;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.FastDateFormat;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.oj.common.ErrorCode;
import com.vv.oj.constant.CommonConstant;
import com.vv.oj.exception.BusinessException;
import com.vv.oj.mapper.ContestMapper;
import com.vv.oj.mapper.ContestQuestionMapper;
import com.vv.oj.mapper.ContestQuestionSubmitMapper;
import com.vv.oj.mapper.UserMapper;
import com.vv.oj.model.dto.contestquestion.UserContestRanking;
import com.vv.oj.model.dto.contestquestionsubmit.ContestQuestionSubmitAddRequest;
import com.vv.oj.model.dto.contestquestionsubmit.ContestQuestionSubmitQueryRequest;
import com.vv.oj.model.dto.contestquestionsubmit.ContestRankingQueryRequest;
import com.vv.oj.model.entity.Contest;
import com.vv.oj.model.entity.ContestQuestion;
import com.vv.oj.model.entity.ContestQuestionSubmit;
import com.vv.oj.model.entity.User;
import com.vv.oj.model.enums.ContestQuestionSubmitLanguageEnum;
import com.vv.oj.model.enums.ContestQuestionSubmitStatusEnum;
import com.vv.oj.model.vo.ContestQuestionSubmitVO;
import com.vv.oj.model.vo.ContestQuestionVO;
import com.vv.oj.model.vo.ContestRankingVO;
import com.vv.oj.model.vo.UserVO;
import com.vv.oj.service.ContestQuestionService;
import com.vv.oj.service.ContestQuestionSubmitService;
import com.vv.oj.service.UserService;
import com.vv.oj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author zyz19
* @description 针对表【contest_contestQuestion_submit(赛事题目提交表)】的数据库操作Service实现
 * @createDate 2023-08-25 19:55:00
 */
@Service
public class ContestQuestionSubmitServiceImpl extends ServiceImpl<ContestQuestionSubmitMapper, ContestQuestionSubmit>
        implements ContestQuestionSubmitService {
    @Resource
    private ContestQuestionService contestQuestionService;

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ContestQuestionMapper contestQuestionMapper;

    @Resource
    private ContestMapper contestMapper;

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

    @Override
    public Page<ContestRankingVO> getContestRankingVO(ContestRankingQueryRequest contestRankingQueryRequest) {
        Long contestId = contestRankingQueryRequest.getContestId();
        long current = contestRankingQueryRequest.getCurrent();
        long size = contestRankingQueryRequest.getPageSize();
        List<ContestRankingVO> rankingVOS = new ArrayList<>();
        Contest contest = contestMapper.selectById(contestId);
        QueryWrapper<ContestQuestionSubmit> queryUserIdWrapper = new QueryWrapper<>();
        queryUserIdWrapper.select("distinct userId");
        queryUserIdWrapper.eq("contestId", contestId);
        Page<ContestQuestionSubmit> contestQuestionSubmitPage = page(new Page<>(current, size), queryUserIdWrapper);
        //根据这个分页里面的数据，各自查询提交记录
        List<Long> userIds = contestQuestionSubmitPage
                .getRecords()
                .stream()
                .map(ContestQuestionSubmit::getUserId)
                .collect(Collectors.toList());
        //查询出每道题目的信息
        QueryWrapper<ContestQuestion> contestQuestionQueryWrapper = new QueryWrapper<>();
        contestQuestionQueryWrapper.select("id").eq("contestId", contestId);
        List<ContestQuestion> contestQuestions = contestQuestionMapper.selectList(contestQuestionQueryWrapper);
        List<Long> contestQuestionIds = contestQuestions.stream().map(ContestQuestion::getId).collect(Collectors.toList());

        //查出每道题第一个AC的人，后续可以放入缓存
        HashMap<Long, Long> firstAcUser = new HashMap<>();
        for (Long contestQuestionId : contestQuestionIds) {
            QueryWrapper<ContestQuestionSubmit> queryFirstSubmit = new QueryWrapper<>();
            queryFirstSubmit
                    .select("userId")
                    .eq("contestQuestionId", contestQuestionId)
                    .eq("result", 0)
                    .orderByAsc("createTime");
            List<ContestQuestionSubmit> list = list(queryFirstSubmit);
            if (list.size() > 0) {
                firstAcUser.put(contestQuestionId, list.get(0).getUserId());
            }
        }

        for (Long userId : userIds) {
            ContestRankingVO contestRankingVO = new ContestRankingVO();

            int totalAcNum = 0;
            int total = 0;
            Map<String, UserContestRanking> map = new HashMap<>();
            for (Long contestQuestionId : contestQuestionIds) {
                //根据每个用户id，查询出对所有题目的提交记录
                UserContestRanking userContestRanking = new UserContestRanking();
                int wrongNum = 0; //对某题错误数
                Date acTime = null; //对某题提交时间
                boolean isAc = false;
                QueryWrapper<ContestQuestionSubmit> queryUserSubmitWrapper = new QueryWrapper<>();
                queryUserSubmitWrapper
                        .eq("userId", userId)
                        .eq("contestQuestionId", contestQuestionId)
                        .eq("contestId", contestId);
                List<ContestQuestionSubmit> list = list(queryUserSubmitWrapper);
                for (ContestQuestionSubmit contestQuestionSubmit : list) {
                    //TODO 统计AC数
                    Integer result = contestQuestionSubmit.getResult();
                    if (result == 0) {
                        isAc = true;
                        acTime = contestQuestionSubmit.getCreateTime();
                    } else if (result != 6){
                        wrongNum++;
                    }
                }
                ContestQuestion contestQuestion = contestQuestionMapper.selectById(contestQuestionId);
                userContestRanking.setDisplayId(contestQuestion.getDisplayId());
                userContestRanking.setTotal(list.size());
                userContestRanking.setWrongNum(wrongNum);
                if(acTime != null){
                    DateTime startTime = DateUtil.parseDate(contest.getStartTime());
                    long diffSeconds = DateUtil.between(acTime, startTime, DateUnit.SECOND);
                    // 将时间差转换为 HH:mm:ss 格式
                    long diffHours = diffSeconds / (60 * 60);
                    long diffMinutes = (diffSeconds % (60 * 60)) / 60;
                    diffSeconds = diffSeconds % 60;
                    String timeDiff = String.format("%02d:%02d:%02d", diffHours, diffMinutes, diffSeconds);
                    userContestRanking.setTotalTime(timeDiff);
                }
                userContestRanking.setAc(isAc);
                //是否是当前题目第一个AC的人
                if (userId.equals(firstAcUser.get(contestQuestionId))) {
                    userContestRanking.setFirst(true);
                }
                //
                if (isAc) {
                    totalAcNum++;
                }
                total += list.size();
                map.put(contestQuestion.getDisplayId(), userContestRanking);
            }
            User user = userMapper.selectById(userId);
            contestRankingVO.setAcNum(totalAcNum);
            contestRankingVO.setTotal(total);
            contestRankingVO.setUserName(user.getUserName());
            contestRankingVO.setUserContestRankingMap(map);
            rankingVOS.add(contestRankingVO);
        }
        Page<ContestRankingVO> rankingVOPage = new Page<>();
        rankingVOPage.setRecords(rankingVOS);
        return rankingVOPage;
    }
}




