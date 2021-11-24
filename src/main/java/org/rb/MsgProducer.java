package org.rb;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Hello world!
 *
 */
//第一个 生产者 代码
public class MsgProducer
{
    private static final String QUEEN_NAME="hello";
    public static void main( String[] args )throws Exception {
        String msg = "Hello World";
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
        //创建队列
        //参数说明
        /***
        1:队列名称  queue
        2:是否持久化持久化会保存到磁盘，默认是保存到内存 durable
        3:是否多个消费者共享消费 exclusive
        4:是否自动删除 autoDelete
        5:其他参数，延迟或者死信队列等  arguments
        ***/
        channel.queueDeclare(QUEEN_NAME,false,false, false,null);
        /**
         * 1: 发送到那个交换机  exchange
         * 2：路由的key值 routingKey
         * 3: 其他参数信息 AMQP.BasicProperties
         * 4：消息参数内容 body
         * **/
        channel.basicPublish("",QUEEN_NAME,null,msg.getBytes());
        System.out.println("消息發送完畢");
    }
}
