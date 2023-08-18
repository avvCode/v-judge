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
) comment '用户' collate = utf8mb4_unicode_ci;

-- 题目表
create table if not exists question
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '题目',
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
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题目点赞表（硬删除）
create table if not exists question_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    questionId     bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (questionId),
    index idx_userId (userId)
) comment '题目点赞';

-- 题目收藏表（硬删除）
create table if not exists question_favour
(
    id         bigint auto_increment comment 'id' primary key,
    questionId     bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (questionId),
    index idx_userId (userId)
) comment '题目收藏';

-- 题目提交表
create table if not exists question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    judgeInfo  text                               null comment '判断信息（json对象）',
    status     int      default 0                 not null comment '判题状态',
    questionId bigint                             not null comment '题目id',
    userId     bigint                             not null comment '创建用 id',
    code       text                               not null comment '用户代码',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目提交';

-- 个人题单
create table if not exists question_group
(
    id         bigint auto_increment comment 'id' primary key,
    title   varchar(128)                       not null comment '题单名字',
    description text                            not null comment '题单描述',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '个人题单';

-- 个人题单 与 题目关系表
create table if not exists question_group_question
(
    id         bigint auto_increment comment 'id' primary key,
    questionId   varchar(128)                       not null comment '题目id',
    groupId     bigint                            not null comment '题单id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (groupId)
) comment '个人题单';

-- 赛事表
create table if not exists contest
(
    id         bigint auto_increment comment 'id' primary key,
    title           varchar(64)                       not null comment '赛事名称',
    description   varchar(128)                       not null comment '赛事描述',
    startTime   varchar(128)                       not null comment '开始时间',
    endTime     varchar(128)                       not null comment '结束时间',
    userId     bigint                             not null comment '创建用户 id',
    joinNum     int     default 0                 null comment '参赛人数',
    status     int      default 0                 not null comment '赛事状态 0-锁定 1-正在进行 2-结束',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '赛事表';

-- 赛事题目表
create table if not exists question
(
    id          bigint auto_increment comment 'id' primary key,
    contestId  bigint                             not null    comment '赛事id' primary key,
    title       varchar(512)                       null comment '题目',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    answer      text                               null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    judgeCase   text                               null comment '判题用例（json数组）',
    judgeConfig text                               null comment '判题配置（json对象）',
    rate        tinyint                            not null comment '题目难度 0-简单 1-中等 2-困难',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (contestId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 用户参赛记录表
create table if not exists user_contest
(
    id         bigint auto_increment comment 'id' primary key,
    userId     bigint                             not null comment '创建用 id',
    contestId     bigint                             not null comment '赛事id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '用户参赛记录表';

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
) comment '题解' collate = utf8mb4_unicode_ci;

-- 题解点赞表（硬删除）
create table if not exists question_solving_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    questionSolvingId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (questionSolvingId),
    index idx_userId (userId)
) comment '题解点赞';

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
) comment '评论' collate = utf8mb4_unicode_ci;

-- 评论点赞表（硬删除）
create table if not exists question_comment_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    questionCommentId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (questionCommentId),
    index idx_userId (userId)
) comment '评论点赞';

