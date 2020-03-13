import static org.hamcrest.CoreMatchers.instanceOf;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.TextMessage;

import org.apache.activemq.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.atguigu.gmall.util.Constants;

import redis.clients.jedis.Jedis;

@Component
public class JTmq {

	
	
	 @JmsListener(destination = "TextMessage",containerFactory =
			 "jmsQueueListener")
			 public void consumePaymentResultMapMessage(Message message) throws JMSException { 
		 		
		 		if (message instanceof TextMessage) {
					TextMessage textMessage=(TextMessage)message;
					System.out.println(textMessage.getStringProperty("name"));
				}
		 	
	 }
	 
}
