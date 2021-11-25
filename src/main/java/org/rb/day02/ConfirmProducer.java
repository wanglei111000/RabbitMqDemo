package org.rb.day02;

//发布确认
//1: 单个确认
//2:批量确认
//3:异步确认


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import org.rb.util.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmProducer {
    public static int MESSAGE_COUNT = 800;
    public static void main(String[] args) throws Exception{
        //ConfirmProducer.publishMsgSingle(); //发布800个消息耗时1575ms
        //ConfirmProducer.publishMsgBatch();  //发布800个消息耗时242ms ,当出现消息未确认时无法知道那个消息没有被确认
        ConfirmProducer.publishMsgSynchBatch(); //发布800个消息耗时197ms  异步的时间
    }

    //1: 单个确认
    public static void publishMsgSingle() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queenName = UUID.randomUUID().toString();
        channel.queueDeclare(queenName,true,false,false,null);
        channel.confirmSelect(); // 开启发布确认
        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "msg"+i;
            channel.basicPublish("",queenName,null,message.getBytes());
            boolean flag = channel.waitForConfirms();  //等候确认
            if(flag){
              System.out.println("第"+i+"个消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个消息耗时"+(end-start)+"ms");
    }

    //2:批量确认
    public static void publishMsgBatch() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queenName = UUID.randomUUID().toString();
        channel.queueDeclare(queenName,true,false,false,null);
        channel.confirmSelect(); // 开启发布确认
        //批量确认的大小 假设 200条
        int batchSize = 200;
        long start = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "msg"+i;
            channel.basicPublish("",queenName,null,message.getBytes());
            if(i%batchSize == 0 ){
                channel.waitForConfirms();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个消息耗时"+(end-start)+"ms");
    }

    //3:异步确认
    public static void publishMsgSynchBatch() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queenName = UUID.randomUUID().toString();
        channel.queueDeclare(queenName,true,false,false,null);
        channel.confirmSelect(); // 开启发布确认

        //方便批量删除 ，通过序号
        //支持多线程并发
        ConcurrentSkipListMap<Long,String> confirmMap = new ConcurrentSkipListMap<>();

        long start = System.currentTimeMillis();
        //消息成功发送回调函数  deliveryTag  消息标记  multiple 是否批量确认
        ConfirmCallback ackCallback = (deliveryTag, multiple)->{
            //步骤002 删除掉已经被消费的消息
            if(multiple){
                //可能会造成消息丢失 一般不用
                ConcurrentNavigableMap<Long,String> confirmedMap =  confirmMap.headMap(deliveryTag);
                confirmedMap.clear();
            }else{
                //推荐使用这种方式
                confirmMap.remove(deliveryTag);
            }
            System.out.println("正确确认的消息:"+deliveryTag);

        };

        //消息失败回调函数  deliveryTag  消息标记  multiple 是否批量确认
        ConfirmCallback nackCallback = (deliveryTag, multiple)->{
            //这里获取到未处理成功的消息

            //步骤003 处理 步骤002 操作完成之后违未被确认的消息
            String unConfirmMessage = confirmMap.get(deliveryTag);
            System.out.println("未确认的消息:"+deliveryTag+"内容是:"+unConfirmMessage);

        };

        //创建监听器在发送消息之前，监听消息的成功和失败
        channel.addConfirmListener(ackCallback,nackCallback);
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "msg"+i;
            channel.basicPublish("",queenName,null,message.getBytes());
            //步骤001 记录下所有的记录
            confirmMap.put(channel.getNextPublishSeqNo(),message);
        }
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个消息耗时"+(end-start)+"ms");
    }


}
