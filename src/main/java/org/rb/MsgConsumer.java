package org.rb;

import com.rabbitmq.client.*;
//第一个 消费者 代码
public class MsgConsumer {
    private static final String QUEEN_NAME="hello";
    public static void main(String[] args) throws Exception{
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
        /**
         * 1:被消费队列名
         * 2:是否自动应答 true 是自动应答 false 手动应答
         * 3:消费者未成功消费的回调函数
         * 4: 消费者去掉消费的回调
         * */
        //正常获取消息
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        //消费消息被中断
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("消费消息被中断");
        };

        channel.basicConsume(QUEEN_NAME,true,deliverCallback,cancelCallback);
        System.out.println("消息接受完毕");
    }
}
