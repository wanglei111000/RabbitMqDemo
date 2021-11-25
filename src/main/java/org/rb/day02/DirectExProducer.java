package org.rb.day02;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.rb.util.RabbitMqUtils;

import java.util.Scanner;

//演示直接交换机生产者
public class DirectExProducer {
    private static final String DIRECT_EXCHANGE_NAME="DIRECT_EX";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //根据不同的RoutingKey 将消息发送的不同的队列
            channel.basicPublish(DIRECT_EXCHANGE_NAME,"003", null,message.getBytes("UTF-8"));
            //channel.basicPublish(DIRECT_EXCHANGE_NAME,"002", null,message.getBytes("UTF-8"));
            //channel.basicPublish(DIRECT_EXCHANGE_NAME,"003", null,message.getBytes("UTF-8"));
            System.out.println("消息"+message+"发送完成");
        }
    }
}
