## V-Judge

### 介绍
一个在线判题、在线比赛的小型微服务项目，能够根据预先设定的测试用例，编译、运行用户提交的代码进行判断，给予用户是否正确的结果。
其中，判题服务是自主实现的代码评测服务，可以作为独立的服务给予其他用户调用使用。支持在线比赛模式，支持查看实时Ranking排名、
submit提交记录等。

- 支持ACM模式
- 支持本地C、C++、Java判题（安全性、内存管理困难）
- 支持docker的Java判题

后续计划：
- 调整Ranking排名的表格样式。
- 支持go判题
- ……

### 技术栈
前端：

Vue3 + Arco Design组件库 + bytemd MarkDown语法编辑器 + monaco code edit代码编辑器

后端：

开发框架：

SSM + Spring Boot + Spring Cloud Alibaba 

中间件：

Spring Cloud Gateway + Rabbit MQ

数据库：

MySQL

### 项目架构



### 核心模块划分

v-judge-common 公共常量类模块

v-judge-model 公共实体类模块

v-judge-service-client 内部调用接口暴露

用户服务暴露：
```
User getById(@RequestParam("userId") long userId)

List<User> listByIds(@RequestParam("idList") Collection<Long> idList)

default User getLoginUser(HttpServletRequest request)

default boolean isAdmin(User user)

default UserVO getUserVO(User user)
```

判题服务暴露：
```
QuestionSubmit doJudge(submitId)
```


v-judge-gateway-service 网关服务 端口 9000

v-judge-user-service 用户服务 端口 9001

v-judge-question-service 题目服务 9002

v-judge-contest-service 比赛服务（包括比赛、赛题）9003

v-judge-judge-service 判题服务 9004

v-judge-codesandbox 代码沙箱 9005（负责执行代码，给出执行时间、内存、输出用例）

### 已实现功能

部分截图

<img src="doc/题目列表.png">

做题页面

<img src="doc/做题.png">

提交列表

<img src="doc/提交记录.png">

创建题目

<img src="doc/创建题目.png">

个人中心

<img src="doc/个人中心.png">

用户管理

<img src="doc/用户管理.png">

比赛中心

<img src="doc/赛题列表.png">

<img src="doc/赛题提交列表.png">

<img src="doc/提交时间列表.png">

<img src="doc/创建比赛.png">

<img src="doc/编辑比赛.png">