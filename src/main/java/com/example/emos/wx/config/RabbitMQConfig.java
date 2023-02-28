package com.example.emos.wx.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/26 15:27
 * @version: 1.0
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public ConnectionFactory getFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.79"); //Linux主机的IP地址
        factory.setPort(5672); //RabbitMQ端口号
        return factory;
    }
}

