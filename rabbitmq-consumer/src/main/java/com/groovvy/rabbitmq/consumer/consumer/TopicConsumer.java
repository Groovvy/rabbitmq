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
public class TopicConsumer {

    Logger log = LoggerFactory.getLogger(TopicConsumer.class);
    private static final String TOP_QUEUE_NAME="topic-queue";

    @RabbitListener(queues = TOP_QUEUE_NAME)
    public void receiveA(Message message, Channel channel) throws IOException {
        log.info("收到主题消息：" + new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
