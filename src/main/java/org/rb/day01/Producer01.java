package org.rb.day01;

import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.rb.util.RabbitMqUtils;

import java.util.Scanner;

public class Producer01 {
    private static final String QUEEN_NAME="hello";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        /***
         1:队列名称  queue
         2:是否持久化持久化会保存到磁盘，默认是保存到内存 durable
         3:是否多个消费者共享消费 exclusive
         4:是否自动删除 autoDelete
         5:其他参数，延迟或者死信队列等  arguments
         ***/
        channel.queueDeclare(QUEEN_NAME,false,false,false,null);
        //为了发消息比较明显，使用控制台发送
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEEN_NAME,null,message.getBytes());
            System.out.println("消息"+message+"发送完成");
        }
    }
}
