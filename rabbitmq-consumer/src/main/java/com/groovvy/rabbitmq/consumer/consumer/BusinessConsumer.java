package com.groovvy.rabbitmq.consumer.consumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author wanghuaan
 * @date 2020/10/27
 **/
@Component
public class BusinessConsumer {
    Logger log = LoggerFactory.getLogger(BusinessConsumer.class);

    private static final String FANOUT_QUEUE_NAME="fanout-queue";
    @RabbitListener(queues = FANOUT_QUEUE_NAME)
    public void receive(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("收到业务消息A：{}", msg);
        boolean ack = true;
        Exception exception = null;
        try {
            if (msg.contains("deadletter")){
                throw new RuntimeException("dead letter exception");
            }
        } catch (Exception e){
            ack = false;
            exception = e;
        }
        if (!ack){
            log.error("消息消费发生异常，error msg:{}", exception.getMessage(), exception);
            //参数为：消息的DeliveryTag，是否批量拒绝，是否重新入队
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        } else {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
