package com.rabbitmq.rabbitmq_basic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * RabbitMq生产者
 */
public class ProducerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerApp.class);
    private static final String MQ_USERNAME = "";
    private static final String MQ_PASSWORD = "";
    private static final String MQ_BROKER_URL = "localhost";
    private static final int MQ_PORT = 5672;
    private static final String QUEUE_SUBJECT = "CustomQueueKEY";
	public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();  
        
        factory.setHost(MQ_BROKER_URL);
        factory.setPort(MQ_PORT);
        
        Connection connection = factory.newConnection();  
        
        Channel channel = connection.createChannel();  
        
        //声明队列
        boolean durable = true;//消息持久化 
        channel.queueDeclare(QUEUE_SUBJECT, durable, false, false, null);  
        //发送10条消息
        for (int i = 10; i >= 0; i--) {
        	
            String dots = "";
            
            for (int j = 0; j < i; j++) {
            	
                dots += ".";  
            }
            
            String message = dots + dots.length();  
            /*
             * 转发器名称
             * 消息队列KEY
             * MessageProperties.PERSISTENT_TEXT_PLAIN:消息持久化  
             * 消息(字节码)
             * */
            channel.basicPublish("", QUEUE_SUBJECT, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));  
            System.out.println("发送消息: " + message);  
        }
        
        channel.close();  
        connection.close();  
	}
}
