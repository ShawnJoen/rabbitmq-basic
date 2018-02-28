package com.rabbitmq.pubsub.topic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogTopic {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmitLogTopic.class);
    private static final String MQ_USERNAME = "";
    private static final String MQ_PASSWORD = "";
    private static final String MQ_BROKER_URL = "localhost";
    private static final int MQ_PORT = 5672;
    private static final String QUEUE_SUBJECT = "";
    private static final String EXCHANGE_NAME = "EXCHANGE_NO3";
	public static void main(String[] args) throws UnsupportedEncodingException, IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();  
        
        factory.setHost(MQ_BROKER_URL);
        factory.setPort(MQ_PORT);
        
        Connection connection = factory.newConnection();
        
        Channel channel = connection.createChannel();
        /* 声明转发器
         * 类型:[Direct, Topic, Headers, Fanout]
         * topic : 广播到所有它所知道的队列, 
         * 			条件为 指定的所有转发器内指定路由(可以使用标识符来筛选路由)。
         * 				*可以匹配一个标识符
         *				#可以匹配0个或多个标识符
         * */
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");  

        String[] routing_keys = new String[] {
        		"kernel.info", 
        		"cron.warning",
                "auth.info", 
                "kernel.critical"
        };
        
        for (String routing_key : routing_keys) {
        	
            String message = UUID.randomUUID().toString();
            /* * 往转发器上发送消息  
             * 转发器名称
             * routingkey 指定转发器内 指定消息队列(*转发器类型fanout不能使用此参数)
             * 消息持久化: 发布/订阅系统 一般为不为持久化 因此设置 null
             * 消息(字节码)
             * */
            channel.basicPublish(EXCHANGE_NAME, routing_key, null, message.getBytes("UTF-8"));
            
            //System.out.println("发送消息: routingKey = " + routing_key + " , message = " + message);
        }  
  
        channel.close();  
        connection.close();  
	}

}
