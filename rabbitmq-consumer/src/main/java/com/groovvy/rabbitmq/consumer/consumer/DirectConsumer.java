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
public class DirectConsumer {

    Logger log = LoggerFactory.getLogger(DirectConsumer.class);
    private static final String DIRECT_QUEUE_NAME="direct-queue";

    @RabbitListener(queues = DIRECT_QUEUE_NAME)
    public void receiveA(Message message, Channel channel) throws IOException {
        log.info("收到直连消息：" + new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
