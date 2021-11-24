package org.rb.day01;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.rb.util.RabbitMqUtils;

//手动应答,消息不丢失，返回队列个可达的消费者消费
public class NotAutoAckConsumer {
    private static final String NOTAUTOACK_QUEEN_NAME="ack_queen";
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        DeliverCallback deliverCallback = (consumerTag, message)->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /**
             * 手动应答 获取消息标签
             * 和不批量应答
             * **/
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            System.out.println("NotAutoAckConsumer1接受到的消息:"+ new String(message.getBody()));
        };

        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag+"消息被取消回调");
        };

        boolean autoAck = false;
        channel.basicConsume(NOTAUTOACK_QUEEN_NAME,autoAck,deliverCallback,cancelCallback);
    }
}
