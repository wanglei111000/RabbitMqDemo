package org.rb.day02;

//正常的消费者

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.rb.util.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 需要定义 正常的交换机            死信交换机
 *         正常队列                死信队列
 *         正常交换机的RoutingKey   死信的 RoutingKey
 *         以及正常的交换即和正常队列的绑定    死信交换机和死信队列的绑定
 *         以及 发生异常时 将信息发送到死信的操作
 *
 * **/
public class DeadMessageConsumer3 {
    //正常的交换机
    public static final String NORMAL_EXCHANGE = "NORMAL_EXCHANGE";
    //死信交换机
    public static final String DEAD_EXCHANGE = "DEAD_EXCHANGE";

    //正常的交队列
    public static final String NORMAL_QUEUE = "NORMAL_QUEUE";
    //死信交队列
    public static final String DEAD_QUEUE = "DEAD_QUEUE";

    //正常交换机的RoutingKey
    public static final String NORMAL_ROUTING_KEY = "NORMAL_ROUTING_KEY";
    //死信的 RoutingKey
    public static final String DEAD_ROUTING_KEY = "DEAD_ROUTING_KEY";


    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //正常的交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //=======================================
        //接受消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            String msg = new String(message.getBody());
            if(msg.equals("info5")){
                System.out.println("DeadMessageConsumer拒绝接受"+msg);
                //requeue 表示是否重新放回队列,拒绝不放回队列 ，使其进入死信队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else{
                System.out.println("DeadMessageConsumer接受到消息"+new String(message.getBody()));
                //mutiple 设置false 表示不批量应答
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        CancelCallback cancelCallback = (consumerTag)->{
            //未处理
        };
        //正常队列
        //準備給正常隊列的參數
        Map<String,Object> arguments = new HashMap<>();
        //设置不能正常消费时的私信交换机的参数
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信交换机的RoutingKey
        arguments.put("x-dead-letter-routing-key",DEAD_ROUTING_KEY);
        //arguments.put("x-message-ttl",10000); //设置过期时间
        //测试超过最大长度
        //arguments.put("x-max-length",20);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);

        //死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //正常的交换即和正常队列的绑定
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,NORMAL_ROUTING_KEY);
        //死信交换机和死信队列的绑定
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,DEAD_ROUTING_KEY);
        //=======================================
        System.out.println("..........等待接受消息..............");
        //autoAck 为false 不自动应答
        channel.basicConsume(NORMAL_QUEUE,false, deliverCallback, cancelCallback);

    }

}
