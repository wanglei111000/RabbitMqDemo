package org.rb.day02;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.rb.util.RabbitMqUtils;

public class TopicConsumer {
    private static final String TOPIC_EXCHANGE_NAME="TOPIC_EX";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明一个临时队列,队列名称是随机的，当消费者断开连接自动删除
        String queenName = channel.queueDeclare().getQueue();
        //绑定交换机和队列  可以多重绑定
        channel.queueBind(queenName,TOPIC_EXCHANGE_NAME,"*.aaa.www");

        //接受消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("DirectExConsumer01接受到消息"+new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag)->{
            //未处理
        };
        channel.basicConsume(queenName,true,deliverCallback,cancelCallback);
    }
}
