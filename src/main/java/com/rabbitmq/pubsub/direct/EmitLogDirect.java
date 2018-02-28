package com.rabbitmq.pubsub.direct;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmitLogDirect.class);
    private static final String MQ_USERNAME = "";
    private static final String MQ_PASSWORD = "";
    private static final String MQ_BROKER_URL = "localhost";
    private static final int MQ_PORT = 5672;
    private static final String QUEUE_SUBJECT = "";
    private static final String EXCHANGE_NAME = "EXCHANGE_NO2";
    private static final String[] SEVERITIES = {"info", "warning", "error"};
	public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();  
        
        factory.setHost(MQ_BROKER_URL);
        factory.setPort(MQ_PORT);
        
        Connection connection = factory.newConnection();
        
        Channel channel = connection.createChannel();
        /* 声明转发器
         * 类型:[Direct, Topic, Headers, Fanout]
         * direct: 广播到所有它所知道的队列, 
         * 			条件为 指定的所有转发器内 指定routingkey
         * */
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");  

        //发送6条消息  
        for (int i = 0; i < 6; i++) {  
        	//随机获取 routingkey
            String severity = getSeverity();  
            
            String message = severity + "_log :" + UUID.randomUUID().toString();  
            
            //发布消息至转发器，指定routingkey  
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));  
            
            System.out.println("发送消息: " + message); 
        }  
  
        channel.close();  
        connection.close();  
	}
	
    /** 
     * 随机产生一种日志类型 
     * @return 
     */  
    private static String getSeverity() {  
        Random random = new Random();  
        int ranVal = random.nextInt(3);  
        return SEVERITIES[ranVal];  
    }  
}
