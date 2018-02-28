package com.rabbitmq.pubsub.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ReceiveLogsTopicForCritical {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveLogsTopicForCritical.class);
    private static final String MQ_USERNAME = "";
    private static final String MQ_PASSWORD = "";
    private static final String MQ_BROKER_URL = "localhost";
    private static final int MQ_PORT = 5672;
    private static final String QUEUE_SUBJECT = "";
    private static final String EXCHANGE_NAME = "EXCHANGE_NO3";
	public static void main(String[] args) throws IOException, TimeoutException {

        //区分不同工作进程的输出
        final int hashCode = ReceiveLogsTopicForCritical.class.hashCode();
        //创建连接和频道
        final ConnectionFactory factory = new ConnectionFactory();
        
        factory.setHost(MQ_BROKER_URL);
        factory.setPort(MQ_PORT);
        
        final Connection connection = factory.newConnection();
        
        final Channel channel = connection.createChannel();
        //声明direct类型转发器  
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");  
        
        //创建一个非持久的、唯一的且自动删除的队列  
        String randomQueueName = channel.queueDeclare().getQueue();  
        /* 为转发器指定队列，设置binding
         * 第三个参数为 routingkey:
         * 						转发器direct发布的消息 可以定义后条件性订阅
         * 						转发器fanout是无效的
         * 						转发器topic时可以使用表示符 来筛选routingkey: *匹配一个标识符,#匹配0个或多个标识符
         * 							topic注意: 不同订阅 条件不同不过都匹配时 都会订阅到消息
         * */
        channel.queueBind(randomQueueName, EXCHANGE_NAME, "kernel.*");  
        
        System.out.println(hashCode + " [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override  
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,  
                    byte[] body) throws IOException {

                String message = new String(body, "UTF-8");
                
                String routingKey = envelope.getRoutingKey(); 
                
                System.out.println("订阅消息 : " + message + ", routingKey = " + routingKey + ", " + hashCode);
            }
        };
        //指定消费队列  
        boolean ack = true;//设置 false 的话 就是 打开应答机制  - false处理完消息 最后需要手动 basicAck发送应答处理
        				   //反之 true为自动应答
        channel.basicConsume(randomQueueName, ack, consumer);  
	}

}
