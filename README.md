# Spring Boot整合RabbtiMQ
Spring Boot集成RabbitMQ

需要在本机安装Erlang、RabbitMQ并运行。

RabbitMQ默认端口：5672。

# 测试内容

- 测试直连交换机:http://localhost:8081/directSender
- 测试主题交换机:http://localhost:8081/topicSender
- 测试广播交换机:http://localhost:8081/deadSender
- 测试死信队列的拒收情况:http://localhost:8081/deadSender
- 测试死信队列的过期情况:http://localhost:8081/delaySender
# 消息会变成死信消息的场景
- 消息被消费者拒绝签收
- 消息过期，过了TTL存活时间
- 队列设置了x-max-length最大消息数量且当前队列中的消息已经达到了这个数量，再次投递，消息将被挤掉，被挤掉的是最靠近被消费那一端的消息。

# 测试方法
1.分别运行consumer和producer项目
2.postman或者浏览器测试发送消息
