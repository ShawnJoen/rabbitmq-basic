package com.rabbitmq.rabbitmq_basic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * RabbitMq消費者
 */
public class ConsumerApp {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerApp.class);
	private static final String MQ_USERNAME = "";
    private static final String MQ_PASSWORD = "";
    private static final String MQ_BROKER_URL = "localhost";
    private static final int MQ_PORT = 5672;
	private static final String SUBJECT = "CustomQueueKEY";
	public static void main(String[] args) throws IOException, TimeoutException {
        //区分不同工作进程的输出
        final int hashCode = ConsumerApp.class.hashCode();
        //创建连接和频道
        final ConnectionFactory factory = new ConnectionFactory();
        
        factory.setHost(MQ_BROKER_URL);
        factory.setPort(MQ_PORT);
        
        final Connection connection = factory.newConnection();
        
        final Channel channel = connection.createChannel();
        
        //声明队列 
        boolean durable = true; //消息持久化  
        channel.queueDeclare(SUBJECT, durable, false, false, null);
        System.out.println(hashCode + " [*] Waiting for messages. To exit press CTRL+C");
        //设置最大服务转发消息数量  
        int prefetchCount = 1;//一个消费者同一时间只能拿一条消息 意思是 消费者空闲的时候 才能拿消息进行处理
        channel.basicQos(prefetchCount);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override  
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,  
                    byte[] body) throws IOException {

                String message = new String(body, "UTF-8");  
                
                System.out.println("接收消息 开始处理: " + message + ", " + hashCode);
                
                try {
                	//等待一秒
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
                
                System.out.println("接收消息 结束处理: " + message + ", " + hashCode);
                //发送应答
                channel.basicAck(envelope.getDeliveryTag(), false);  
            }
        };
        //指定消费队列  
        boolean ack = false; //打开应答机制  
        channel.basicConsume(SUBJECT, ack, consumer);  
	}
}
