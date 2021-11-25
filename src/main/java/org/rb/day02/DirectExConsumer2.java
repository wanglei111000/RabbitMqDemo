package org.rb.day02;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.rb.util.RabbitMqUtils;

//演示直接交换机消费者
public class DirectExConsumer2 {
    private static final String DIRECT_EXCHANGE_NAME="DIRECT_EX";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个临时队列,队列名称是随机的，当消费者断开连接自动删除
        String queenName = channel.queueDeclare().getQueue();
        //绑定交换机和队列  可以多重绑定
        channel.queueBind(queenName,DIRECT_EXCHANGE_NAME,"003");
        //接受消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("DirectExConsumer2接受到消息"+new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag)->{
            //未处理
        };
        channel.basicConsume(queenName,true,deliverCallback,cancelCallback);
    }
}
