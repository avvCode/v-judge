package com.vv.model.vo;

import com.vv.oj.model.dto.contestquestion.UserContestRanking;
import com.vv.oj.model.entity.ContestQuestionSubmit;
import com.vv.oj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vv
 */
@Data
public class ContestRankingVO {
    private Integer acNum;

    private Integer total;

    private String userName;

    //key 题目
    //value 每道题的用户信息
    private Map<String, UserContestRanking> userContestRankingMap;

}
