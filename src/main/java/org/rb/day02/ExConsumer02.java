package org.rb.day02;


import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.rb.util.RabbitMqUtils;

public class ExConsumer02 {
    private static final String EXCHANGE_NAME="EX";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个临时队列,队列名称是随机的，当消费者断开连接自动删除
        String queenName = channel.queueDeclare().getQueue();
        //绑定交换机和队列
        channel.queueBind(queenName,EXCHANGE_NAME,"");
        //接受消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
           System.out.println("ExConsumer02接受到消息"+new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag)->{
            //未处理
        };
        channel.basicConsume(queenName,true,deliverCallback,cancelCallback);
    }
}
