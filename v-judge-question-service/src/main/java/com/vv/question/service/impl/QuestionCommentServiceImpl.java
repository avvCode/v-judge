package com.vv.question.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.model.entity.QuestionComment;
import com.vv.question.mapper.QuestionCommentMapper;
import com.vv.question.service.QuestionCommentService;
import org.springframework.stereotype.Service;

/**
* @author zyz19
* @description 针对表【question_comment(评论)】的数据库操作Service实现
* @createDate 2023-08-18 18:24:40
*/
@Service
public class QuestionCommentServiceImpl extends ServiceImpl<QuestionCommentMapper, QuestionComment>
    implements QuestionCommentService {

}




