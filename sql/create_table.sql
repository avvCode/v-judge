# 数据库初始化
# @author vv

-- 创建库
create database if not exists judge;

-- 切换库
use judge;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    acNum        bigint       default 0                 null comment '题目通过数量'
) comment '用户表' collate = utf8mb4_unicode_ci;

-- 题目表
create table if not exists question
(
    id          bigint auto_increment comment 'id' primary key,
    displayId   varchar(128)                   not null comment '展现时题目名称',
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    answer      text                               null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    judgeCase   text                               null comment '判题用例（json数组）',
    judgeConfig text                               null comment '判题配置（json对象）',
    rate        tinyint                            not null comment '题目难度 0-简单 1-中等 2-困难',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目表' collate = utf8mb4_unicode_ci;

-- 题目点赞表（硬删除）
create table if not exists question_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    questionId     bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目点赞表（硬删除）';

-- 题目收藏表（硬删除）
create table if not exists question_favour
(
    id         bigint auto_increment comment 'id' primary key,
    questionId     bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目收藏表（硬删除）';

-- 题目提交表
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    judgeInfo  text                               null comment '判断信息（json对象）',
    status     int      default 0                 not null comment '判题状态',
    questionId bigint                             not null comment '题目id',
    userId     bigint                             not null comment '创建用户 id',
    code       text                               not null comment '用户代码',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目提交表';

-- 赛事表
create table if not exists contest
(
    id            bigint auto_increment comment 'id' primary key,
    title         varchar(64)                           not null comment '赛事名称',
    description   varchar(128)                          not null comment '赛事描述',
    announcements text                                  null comment '赛事公告',
    type          int         default 0                 null comment '赛事类型 0-公共 1-私有',
    password      varchar(12) default ''                null comment '密码，公共无，私有有',
    rules         int         default 0                 null comment '赛事规则 0-ACM 1-OI',
    startTime     varchar(128)                          not null comment '开始时间',
    endTime       varchar(128)                          not null comment '结束时间',
    userId        bigint                                not null comment '创建用户 id',
    joinNum       int         default 0                 null comment '参赛人数',
    status        int         default 0                 not null comment '赛事状态 0-锁定 1-开启 2-正在进行 3-结束',
    createTime    datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint     default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '赛事表';

-- 赛事题目表
create table if not exists contest_question
(
    id          bigint auto_increment comment 'id' primary key,
    displayId   varchar(128)                   not null comment '展现时题目名称',
    contestId  bigint                              not null    comment '赛事id',
    title       varchar(512)                       null comment '题目',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    userId     bigint                              not null comment '创建用户 id',
    answer      text                               null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    judgeCase   text                               null comment '判题用例（json数组）',
    judgeConfig text                               null comment '判题配置（json对象）',
    rate        tinyint                            not null comment '题目难度 0-简单 1-中等 2-困难',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_contestId (contestId)
) comment '赛事题目表' collate = utf8mb4_unicode_ci;

-- 用户参赛记录表
create table if not exists user_contest
(
    id         bigint auto_increment comment 'id' primary key,
    userId     bigint                                not null comment '创建用户id',
    contestId  bigint                                not null comment '赛事id',
    acceptNum  int         default 0                 null comment 'AC数',
    total      int         default 0                 null comment '总提交数',
    ranking    int         default 0                 null comment '排名',
    totalTime  varchar(32) default 0                 null comment '总耗时',
    status     tinyint     default 0                 not null comment '比赛状态 0-正在参加（不显示） 1-结束（显示）',
    createTime datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint     default 0                 not null comment '是否删除',
    index idx_userId (userId),
    index idx_contestId (contestId)
) comment '用户参赛记录表';

-- 赛事题目提交表
create table if not exists contest_question_submit
(
    id                bigint auto_increment comment 'id' primary key,
    language          varchar(128)                       not null comment '编程语言',
    judgeInfo         text                               null comment '判断信息（json对象）',
    status            int      default 0                 not null comment '判题状态',
    contestQuestionId bigint                             not null comment '赛事题目id',
    contestId         bigint                             not null comment '赛事id',
    userId            bigint                             not null comment '创建用户id',
    code              text                               not null comment '用户代码',
    createTime        datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete          tinyint  default 0                 not null comment '是否删除',
    index idx_contest_questionId (contestQuestionId),
    index idx_userId (userId)
) comment '赛事题目提交表';

-- 题解表
create table if not exists question_solving
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题解表' collate = utf8mb4_unicode_ci;

-- 题解点赞表（硬删除）
create table if not exists question_solving_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    questionSolvingId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_questionSolvingId (questionSolvingId),
    index idx_userId (userId)
) comment '题解点赞表（硬删除）';

-- 评论表
create table if not exists question_comment
(
    id         bigint auto_increment comment 'id' primary key,
    parentId  bigint                             not null comment '父评论id',
    content    text                               null comment '内容',
    thumbNum   int      default 0                 not null comment '点赞数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '评论表' collate = utf8mb4_unicode_ci;

-- 评论点赞表（硬删除）
create table if not exists question_comment_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    questionCommentId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_questionCommentId (questionCommentId),
    index idx_userId (userId)
) comment '评论点赞表（硬删除）';

