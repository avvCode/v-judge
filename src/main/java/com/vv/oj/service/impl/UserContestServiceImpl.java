package com.vv.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vv.oj.model.entity.UserContest;
import com.vv.oj.service.UserContestService;
import com.vv.oj.mapper.UserContestMapper;
import org.springframework.stereotype.Service;

/**
* @author vv
* @description 针对表【user_contest(用户参赛记录表)】的数据库操作Service实现
* @createDate 2023-08-18 16:54:18
*/
@Service
public class UserContestServiceImpl extends ServiceImpl<UserContestMapper, UserContest>
    implements UserContestService{

}




