package org.rb.day01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.rb.util.RabbitMqUtils;

import java.util.Scanner;

//持久化队列
public class PersistProducer2 {
    private static final String NOTAUTOACK_QUEEN_NAME="persist_queen";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // durable true 表示 开启持队列久化
        channel.queueDeclare(NOTAUTOACK_QUEEN_NAME,true,false,false,null);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //开启消息持久化 MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("",NOTAUTOACK_QUEEN_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("消息"+message+"发送完成");
        }
    }
}
