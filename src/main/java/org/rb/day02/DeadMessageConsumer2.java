package org.rb.day02;

//正常的消费者
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.rb.util.RabbitMqUtils;
public class DeadMessageConsumer2 {

    //死信交队列
    public static final String DEAD_QUEUE = "DEAD_QUEUE";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("DeadMessageConsumer2接受到消息"+new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag)->{
            //未处理
        };
        //=======================================
        System.out.println("..........等待接受消息..............");
        channel.basicConsume(DEAD_QUEUE,true, deliverCallback, cancelCallback);

    }

}
