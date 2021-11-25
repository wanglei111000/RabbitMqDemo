package org.rb.day02;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.rb.util.RabbitMqUtils;

import java.util.Scanner;

public class TopicProducer {
    private static final String TOPIC_EXCHANGE_NAME="TOPIC_EX";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(TOPIC_EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(TOPIC_EXCHANGE_NAME,"*.aaa.*", null,message.getBytes("UTF-8"));
//            channel.basicPublish(TOPIC_EXCHANGE_NAME,"#.aaa.*", null,message.getBytes("UTF-8"));
//            channel.basicPublish(TOPIC_EXCHANGE_NAME,"*.aaa.*", null,message.getBytes("UTF-8"));
//            channel.basicPublish(TOPIC_EXCHANGE_NAME,"*.aaa.#", null,message.getBytes("UTF-8"));
 //            channel.basicPublish(TOPIC_EXCHANGE_NAME,"#.aaa.#", null,message.getBytes("UTF-8"));
            System.out.println("消息"+message+"发送完成");
        }
    }
}
