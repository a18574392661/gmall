package com.atguigu.gmall.pay.demo;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ZhenGuo2 {
    public static void main(String[] args) {
        ConnectionFactory connect = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD,"tcp://localhost:61616");
        try {
            Connection connection = connect.createConnection();
            connection.setClientID("dasdas");
            connection.start();
            //第一个值表示是否使用事务，如果选择true，第二个值相当于选择0
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic testtopic = session.createTopic("speaking");

            // 将话题的消费者持久化
            MessageConsumer consumer = session.createDurableSubscriber(testtopic,"dasdas");

            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if(message instanceof TextMessage){
                        try {
                            String text = ((TextMessage) message).getText();
                            System.err.println(text+"我来了，我来执行。。。我是第二个");

                            // session.commit();
                            // session.rollback();
                        } catch (JMSException e) {
                            // TODO Auto-generated catch block
                            // session.rollback();
                            e.printStackTrace();
                        }
                    }
                }
            });


        }catch (Exception e){
            e.printStackTrace();;
        }
    }

}
