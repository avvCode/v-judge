## V-Judge

### 介绍
一个在线判题、在线比赛的小型微服务项目，能够根据预先设定的测试用例，编译、运行用户提交的代码进行判断，给予用户是否正确的结果。
其中，判题服务是自主实现的代码评测服务，可以作为独立的服务给予其他用户调用使用。支持在线比赛模式，支持查看实时Ranking排名、
submit提交记录等。

后续计划：支持ACM代码模式，调整Ranking排名的表格样式。太难看了

### 技术栈
前端：

Vue3 + Arco Design组件库 + bytemd MarkDown语法编辑器 + monaco code edit代码编辑器

后端：

开发框架：

SSM + Spring Boot + Spring Cloud Alibaba 

中间件：

Spring Cloud Gateway + Rabbit MQ

数据库：

Redis + MySQL

### 项目架构图

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

### 已实现功能