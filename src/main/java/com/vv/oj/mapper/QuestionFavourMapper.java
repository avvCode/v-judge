package com.vv.oj.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vv.oj.model.entity.Post;
import com.vv.oj.model.entity.Question;
import com.vv.oj.model.entity.QuestionFavour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author zyz19
* @description 针对表【question_favour(题目收藏)】的数据库操作Mapper
* @createDate 2023-08-18 18:42:37
* @Entity com.vv.oj.model.entity.QuestionFavour
*/
public interface QuestionFavourMapper extends BaseMapper<QuestionFavour> {
    /**
     * 分页查询收藏帖子列表
     *
     * @param page
     * @param queryWrapper
     * @param favourUserId
     * @return
     */
    Page<Question> listFavourQuestionByPage(IPage<Question> page, @Param(Constants.WRAPPER) Wrapper<Question> queryWrapper,
                                    long favourUserId);
}




