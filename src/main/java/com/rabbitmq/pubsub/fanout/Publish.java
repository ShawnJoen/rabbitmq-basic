package com.rabbitmq.pubsub.fanout;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publish {

    private static final Logger LOGGER = LoggerFactory.getLogger(Publish.class);
    private static final String MQ_USERNAME = "";
    private static final String MQ_PASSWORD = "";
    private static final String MQ_BROKER_URL = "localhost";
    private static final int MQ_PORT = 5672;
    private static final String QUEUE_SUBJECT = "";
    private static final String EXCHANGE_NAME = "EXCHANGE_NO1";
	public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();  
        
        factory.setHost(MQ_BROKER_URL);
        factory.setPort(MQ_PORT);
        
        Connection connection = factory.newConnection();
        
        Channel channel = connection.createChannel();
        /* 声明转发器
         * 类型:[Direct, Topic, Headers, Fanout]
         * fanout: 广播到所有它所知道的队列, 
         * 			条件为 指定的所有转发器。
         * */
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");  

        String message = new Date().toString() + " : log something";
        /* * 往转发器上发送消息  
         * 转发器名称
         * routingkey 指定转发器内 指定消息队列(*转发器类型fanout不能使用此参数)
         * 消息持久化: 发布/订阅系统 一般为不为持久化 因此设置 null
         * 消息(字节码)
         * */
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
  
        System.out.println("发送消息: " + message);
  
        channel.close();  
        connection.close();  
	}

}
