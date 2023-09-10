package com.vv.contest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vv.model.entity.ContestQuestionSubmit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zyz19
* @description 针对表【contest_question_submit(题目提交)】的数据库操作Mapper
* @createDate 2023-08-23 19:02:57
* @Entity com.vv.oj.model.entity.ContestQuestionSubmit
*/
public interface ContestQuestionSubmitMapper extends BaseMapper<ContestQuestionSubmit> {
    List<String> getAllUser(@Param("contestId")Integer contestId);
}




