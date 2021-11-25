package org.rb.day02;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.rb.util.RabbitMqUtils;

import java.util.Scanner;

public class ExProducer {
    private static final String EXCHANGE_NAME="EX";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"", null,message.getBytes("UTF-8"));
            System.out.println("消息"+message+"发送完成");
        }
    }
}
