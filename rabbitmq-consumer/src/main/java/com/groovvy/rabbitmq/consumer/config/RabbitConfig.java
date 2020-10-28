package com.groovvy.rabbitmq.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySource("classpath:rabbitmq.properties")
public class RabbitConfig {

    @Value("${rabbitmq.exchange.direct}")
    private String DIRECT_EXCHANGE_NAME;

    @Value("${rabbitmq.exchange.topic}")
    private String TOPIC_EXCHANGE_NAME;

    @Value("${rabbitmq.exchange.fanout}")
    private String FANOUT_EXCHANGE_NAME;

    @Value("${rabbitmq.exchange.dead}")
    private String DEAD_EXCHANGE_NAME;

    @Value("${rabbitmq.queue.direct}")
    private String DIRECT_QUEUE_NAME;

    @Value("${rabbitmq.queue.topic}")
    private String TOPIC_QUEUE_NAME;

    @Value("${rabbitmq.queue.fanout}")
    private String FANOUT_QUEUE_NAME;

    @Value("${rabbitmq.queue.dead}")
    private String DEAD_QUEUE_NAME;

    @Value("${rabbitmq.queue.delay}")
    private String DELAY_QUEUE_NAME;

    @Value("${rabbitmq.queue.maxlength}")
    private String MAXLENGTH_QUEUE_NAME;

    @Bean("directExchange")
    public DirectExchange getDirectExchange(){
        return new DirectExchange(DIRECT_EXCHANGE_NAME);
    }

    @Bean("directQueue")
    public Queue getDirectQueue(){
        return new Queue(DIRECT_QUEUE_NAME);
    }

    @Bean
    public Binding bindBusiness(@Qualifier("directQueue") Queue queue, @Qualifier("directExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("groovvy.direct");
    }


    @Bean("topicExchange")
    public TopicExchange getTopicExchange(){
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean("topicQueue")
    public Queue getTopicQueue(){
        return new Queue(TOPIC_QUEUE_NAME);
    }
    @Bean
    public Binding bindTopic(@Qualifier("topicQueue") Queue queue, @Qualifier("topicExchange") TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("#.groovvy.#");
    }



    //死信交换机
    @Bean("deadExchange")
    public DirectExchange getDeadExchange(){
        return new DirectExchange(DEAD_EXCHANGE_NAME);
    }

    //死信队列
    @Bean("deadQueue")
    public Queue getDeadQueue(){
        return new Queue(DEAD_QUEUE_NAME);
    }

    //绑定关系
    @Bean
    public Binding deadLetterBinding(@Qualifier("deadQueue") Queue queue,
                                      @Qualifier("deadExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("deadletter.fanoutQueue.routingkey");
    }


    @Bean("fanoutExchange")
    public FanoutExchange getFanoutExchange(){
        return  new FanoutExchange(FANOUT_EXCHANGE_NAME);
    }

    //广播队列
    @Bean("fanoutQueue")
    public Queue businessQueue(){
        Map<String,Object> map = new HashMap<>();
        //       x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        map.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        //       x-dead-letter-routing-key  这里声明当前队列的死信路由key
        map.put("x-dead-letter-routing-key", "deadletter.fanoutQueue.routingkey");
        return QueueBuilder.durable(FANOUT_QUEUE_NAME).withArguments(map).build();
    }

    @Bean
    public Binding bindFanoutQueue(@Qualifier("fanoutQueue") Queue queue, @Qualifier("fanoutExchange") FanoutExchange exchange){
        return BindingBuilder.bind(queue).to(exchange);
    }

    //测试超时
    @Bean("delayQueue")
    public Queue delayQueue(){
        Map<String,Object> map = new HashMap<>();
        map.put("x-message-ttl",10000);
        map.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        map.put("x-dead-letter-routing-key", "deadletter.fanoutQueue.routingkey");
        return QueueBuilder.durable(DELAY_QUEUE_NAME).withArguments(map).build();
    }

    @Bean
    public Binding bindDelayQueue(@Qualifier("delayQueue") Queue queue, @Qualifier("directExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("groovvy.delay");
    }

    //测试最大长度
    @Bean("maxlengthQueue")
    public Queue maxlengthQueue(){
        Map<String,Object> map = new HashMap<>();
        map.put("x-max-length",3);
        map.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        map.put("x-dead-letter-routing-key", "deadletter.fanoutQueue.routingkey");
        return QueueBuilder.durable(MAXLENGTH_QUEUE_NAME).withArguments(map).build();
    }

    @Bean
    public Binding bindMaxlengthQueue(@Qualifier("maxlengthQueue") Queue queue, @Qualifier("directExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("groovvy.maxlength");
    }


}
