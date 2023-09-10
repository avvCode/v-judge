package com.vv.contest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author vv
 */
@SpringBootApplication
@MapperScan("com.vv.contest.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.vv")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.vv.service"})
public class VJudgeContestApplication {
    public static void main(String[] args) {
        SpringApplication.run(VJudgeContestApplication.class, args);
    }
}