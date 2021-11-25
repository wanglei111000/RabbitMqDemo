package org.rb.day02;

import com.rabbitmq.client.Channel;
import org.rb.util.RabbitMqUtils;

//测试死信队列
public class DeadMessageProducer {
    //正常的交换机
    public static final String NORMAL_EXCHANGE = "NORMAL_EXCHANGE";
    //正常交换机的RoutingKey
    public static final String NORMAL_ROUTING_KEY = "NORMAL_ROUTING_KEY";


    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //定义消息被消费的过期时间
        //超过这个时间 还没收到ack 将会把信息 丢入到死信队列
        for (int i = 0; i < 20; i++) {
            String message = "info"+i;
           // channel.basicPublish(NORMAL_EXCHANGE,NORMAL_ROUTING_KEY,properties,message.getBytes());
          //测试超过做大长度
           channel.basicPublish(NORMAL_EXCHANGE,NORMAL_ROUTING_KEY,null,message.getBytes());
        }

    }
}
