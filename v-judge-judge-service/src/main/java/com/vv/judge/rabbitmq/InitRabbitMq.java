package com.vv.judge.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于创建测试程序用到的交换机和队列（只用在程序启动前执行一次）
 */
@Slf4j
public class InitRabbitMq {

    public static void doInit() {

        try {
            //创建一个连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //连接rabbitmq队列
            factory.setHost("106.55.197.233");
            //用户名
            factory.setUsername("vv");
            //密码
            factory.setPassword("wei1712531751");
            //设置虚拟主机
            factory.setVirtualHost("my_vhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = "judge_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名称
            String QUEUE_NAME = "question";
            String ROUTING_KEY = "question";

            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            log.info("消息队列启动成功");
        } catch (Exception e) {
            log.error("消息队列启动失败");
        }
    }

    public static void main(String[] args) {
        doInit();
    }
}
