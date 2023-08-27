package com.vv.oj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vv.oj.mapper.ContestQuestionMapper;
import com.vv.oj.mapper.ContestQuestionSubmitMapper;
import com.vv.oj.mapper.UserMapper;
import com.vv.oj.model.dto.contestquestion.UserContestRanking;
import com.vv.oj.model.entity.ContestQuestion;
import com.vv.oj.model.entity.ContestQuestionSubmit;
import com.vv.oj.model.entity.User;
import com.vv.oj.model.enums.ContestQuestionSubmitStatusEnum;
import com.vv.oj.model.vo.ContestRankingVO;
import com.vv.oj.service.ContestQuestionSubmitService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author vv
 */
@SpringBootTest
class ContestQuestionSubmitServiceImplTest {

    @Resource
    private ContestQuestionSubmitService contestQuestionSubmitService;
    @Resource
    private ContestQuestionMapper contestQuestionMapper;

    @Resource
    private UserMapper userMapper;

    @Test
    void getContestRankingVO() {
        int contestId = 1;
        int current = 1;
        int size = 10;
        QueryWrapper<ContestQuestionSubmit> queryUserIdWrapper = new QueryWrapper<>();
        queryUserIdWrapper.select("distinct userId");
        queryUserIdWrapper.eq("contestId", contestId);
        Page<ContestQuestionSubmit> contestQuestionSubmitPage = contestQuestionSubmitService
                .page(new Page<>(current, size), queryUserIdWrapper);
        //根据这个分页里面的数据，各自查询提交记录
        List<Long> userIds = contestQuestionSubmitPage
                .getRecords()
                .stream()
                .map(ContestQuestionSubmit::getUserId)
                .collect(Collectors.toList());
        //查询出每道题目的信息
        QueryWrapper<ContestQuestion> contestQuestionQueryWrapper = new QueryWrapper<>();
        contestQuestionQueryWrapper.select("id").eq("contestId", contestId) ;
        List<ContestQuestion> contestQuestions = contestQuestionMapper.selectList(contestQuestionQueryWrapper);
        List<Long> contestQuestionIds = contestQuestions.stream().map(ContestQuestion::getId).collect(Collectors.toList());

        //查出每道题第一个AC的人，后续可以放入缓存
        HashMap<Long,Long> firstAcUser = new HashMap<>();
        for (Long contestQuestionId : contestQuestionIds) {
            QueryWrapper<ContestQuestionSubmit> queryFirstSubmit = new QueryWrapper<>();
            queryFirstSubmit
                    .select("userId")
                    .eq("contestQuestionId", contestQuestionId)
                    //TODO 这个状态有待商榷
                    .eq("status", 0)
                    .orderByAsc("createTime");
            List<ContestQuestionSubmit> list = contestQuestionSubmitService.list(queryFirstSubmit);
            if(list.size() > 0){
                firstAcUser.put(contestQuestionId, list.get(0).getUserId());
            }
        }

        for (Long userId : userIds) {
            ContestRankingVO contestRankingVO = new ContestRankingVO();

            int totalAcNum = 0;
            int total = 0;
            Map<String,UserContestRanking> map = new HashMap<>();
            for (Long contestQuestionId : contestQuestionIds) {
                //根据每个用户id，查询出对所有题目的提交记录
                UserContestRanking userContestRanking = new UserContestRanking();
                int wrongNum = 0; //对某题错误数
                String createTime = ""; //对某题提交时间
                boolean isAc = false;
                QueryWrapper<ContestQuestionSubmit> queryUserSubmitWrapper = new QueryWrapper<>();
                queryUserSubmitWrapper
                        .eq("userId",userId)
                        .eq("contestQuestionId",contestQuestionId)
                        .eq("contestId",contestId);
                List<ContestQuestionSubmit> list = contestQuestionSubmitService.list(queryUserSubmitWrapper);
                for (ContestQuestionSubmit contestQuestionSubmit : list) {
                    //TODO 统计AC数
                    if(contestQuestionSubmit.getStatus() == 0){
                        isAc = true;
                    }else{
                        wrongNum++;
                    }
                }
                ContestQuestion contestQuestion = contestQuestionMapper.selectById(contestQuestionId);
                userContestRanking.setDisplayId(contestQuestion.getDisplayId());
                userContestRanking.setTotal(list.size());
                userContestRanking.setWrongNum(wrongNum);
                userContestRanking.setTotalTime(createTime);
                userContestRanking.setAc(isAc);
                //是否是当前题目第一个AC的人
                if(userId.equals(firstAcUser.get(contestQuestionId))){
                    userContestRanking.setFirst(true);
                }
                //
                if(isAc){
                    totalAcNum++;
                }
                total+= list.size();
                map.put(contestQuestion.getDisplayId(),userContestRanking);
            }
            User user = userMapper.selectById(userId);
            contestRankingVO.setAcNum(totalAcNum);
            contestRankingVO.setTotal(total);
            contestRankingVO.setUserName(user.getUserName());
            contestRankingVO.setUserContestRankingMap(map);
        }
    }
}