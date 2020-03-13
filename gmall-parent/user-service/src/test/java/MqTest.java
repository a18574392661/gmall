import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.atguigu.gmall.UserServiceApplication;
import com.atguigu.gmall.mq.ActiveMQUtil;
import com.atguigu.gmall.util.RedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserServiceApplication.class) // 这里的Application是springboot的启动类名
@WebAppConfiguration
public class MqTest {

	@Autowired
	private ActiveMQUtil activeMQUtil;
	
	
	@Test
	public void sendTimeMessag() {
		
		  Connection connection = null;
	        Session session = null;
	        
		try {
			connection=activeMQUtil.getConnectionFactory().createConnection();
			session=connection.createSession(true, Session.SESSION_TRANSACTED);
			//创建一个
			 Queue payhment_success_queue = session.createQueue("TextMessage");
			 MessageProducer producer = session.createProducer(payhment_success_queue);
			
			/*
			 * MapMessage mapMessage = new ActiveMQMapMessage();// hash结构 if (map!=null) {
			 * for (String mkey : map.keySet()) {
			 * System.out.println(mkey+"//"+map.get(mkey));
			 * mapMessage.setString(mkey,map.get(mkey)+"");//次数 订单号 } }
			 */
			 TextMessage TextMessage=new ActiveMQTextMessage();
			 TextMessage.setStringProperty("name", "张三");
			  	//大于0的话 设置延时队列 
			/*
			 * if (time>0) {
			 * mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,time); }
			 */
	            producer.send(TextMessage);

	            session.commit();
	            
		} catch (Exception e) {
			try {
				session.rollback();
			} catch (JMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally {
			try {
				connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}


}
