package org.rb.day01;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.rb.util.RabbitMqUtils;
//消费类
public class Worker01 {
    private static final String QUEEN_NAME="hello";

    public static void main(String[] args) throws Exception{
       Channel channel =  RabbitMqUtils.getChannel();
        /**
         * 1:被消费队列名
         * 2:是否自动应答 true 是自动应答 false 手动应答
         * 3:消费者未成功消费的回调函数
         * 4: 消费者去掉消费的回调
         * */
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("C02接受到的消息:"+ new String(message.getBody()));
        };

        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag+"消息被取消回调");
        };
        //channel.basicQos(1);
        // 设置不公平分发原则，体现一种能者多劳的方式 哪个线程处理的快，就会多处理消息
        channel.basicConsume(QUEEN_NAME,true,deliverCallback,cancelCallback);
    }
}
