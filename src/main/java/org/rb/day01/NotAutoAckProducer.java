package org.rb.day01;

import com.rabbitmq.client.Channel;
import org.rb.util.RabbitMqUtils;

import java.util.Scanner;

public class NotAutoAckProducer {
    private static final String NOTAUTOACK_QUEEN_NAME="ack_queen";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(NOTAUTOACK_QUEEN_NAME,false,false,false,null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",NOTAUTOACK_QUEEN_NAME,null,message.getBytes("UTF-8"));
            System.out.println("消息"+message+"发送完成");
        }
    }
}
