package com.groovvy.rabbitmq.controller;

import com.groovvy.rabbitmq.producer.MessageSender;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wanghuaan
 * @date 2020/10/27
 **/
@Controller
public class ProducerController {
    @Autowired
    AmqpTemplate amqpTemplate;
    @Autowired
    MessageSender messageSender;

    @GetMapping("/directSender")
    @ResponseBody
    public String directSender(){
        amqpTemplate.convertAndSend("direct-exchange","groovvy.direct","我是直连交换机消息");
        return "direct message send success";
    }

    @GetMapping("/topicSender")
    @ResponseBody
    public String topicSender(){
        amqpTemplate.convertAndSend("topic-exchange","a.groovvy.b","我是主题交换机消息 : a.groovvy.b");
        amqpTemplate.convertAndSend("topic-exchange","c.groovvy.d","我是主题交换机消息 : c.groovvy.d");
        return "topic message send success";
    }


    @GetMapping("/deadSender")
    @ResponseBody
    public String deadSender(){
        amqpTemplate.convertAndSend("fanout-exchange","","-------- a fanout msg");
        amqpTemplate.convertAndSend("fanout-exchange","","-------- a fanout deadletter msg");
        return "dead message send success";
    }

    @GetMapping("/delaySender")
    @ResponseBody
    public String delaySender(){
        amqpTemplate.convertAndSend("direct-exchange","groovvy.direct","我是直连交换机消息");
        amqpTemplate.convertAndSend("direct-exchange","groovvy.delay","我是过期未消费消息");

        return "delay message send success";
    }

    @GetMapping("/maxlengthSender")
    @ResponseBody
    public String maxlengthSender(){
        for(int i=0;i<5;i++){
            amqpTemplate.convertAndSend("direct-exchange","groovvy.maxlength","我是直连交换机消息"+(i+1));
        }



        return "maxlength message send success";
    }

    @GetMapping("/confirmSender")
    @ResponseBody
    public String confirmSender(){
        messageSender.send();

        return "confirmSender send success";
    }

}
