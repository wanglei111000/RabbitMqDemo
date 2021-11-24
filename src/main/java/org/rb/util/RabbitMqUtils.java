package org.rb.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 链接工厂创建 channel 信道工具类
 * */
public class RabbitMqUtils {
    public static Channel getChannel()throws Exception{
        //创建链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置工厂配置信息
        factory.setHost("192.168.217.128");
        factory.setPassword("dev123456");
        factory.setUsername("developer");
        //创建链接
        Connection connection = factory.newConnection();
        //创建信道
        Channel channel = connection.createChannel();
        return channel;
    }
}
