package com.atguigu.gmall.manage.service.lmpl;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.PaymentInfo;
import com.atguigu.gmall.manage.mapper.PaymentInfoMapper;
import com.atguigu.gmall.mq.ActiveMQUtil;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.PaymentInfoService;

@Service
public class PaymentInfoServicelmpl implements PaymentInfoService {

	@Autowired
	private PaymentInfoMapper paymentInfoMapper;
	
	@Reference
	private OrderService orderService;
	
	@Override
	public int savePayinfo(PaymentInfo pInfo) {
		// TODO Auto-generated method stub
		//先查询 
		
	PaymentInfo info=paymentInfoMapper.selectOneDisbale(pInfo.getOrderId());
	
		if (info==null) {
			 paymentInfoMapper.insert(pInfo);
		}
	
		 //每次去支付页面都消费延时队列(定时器也行) 去监听支付状态 解决支付宝系统出问题不出现回调的问题
		 Map<String, Object> map=new HashMap<String, Object>();
		 map.put("out_trade_no", pInfo.getOrderId());
		 
		// sendMessag("PAYSTATUS",map);
		 
		 
		return 1;
	}

	@Autowired
	private ActiveMQUtil activeMQUtil;
	

	@Override
	public Map<String, Object> queryPayStatuc(String orderId, int count) {
	
		return null;
	}
	
	
	
	public void sendTimeMessag(String sendName,Map<String, Object> map,int time) {
		System.out.println(activeMQUtil+"cnm");
		  Connection connection = null;
	        Session session = null;
	        
		try {
			connection=activeMQUtil.getConnectionFactory().createConnection();
			session=connection.createSession(true, Session.SESSION_TRANSACTED);
			//创建一个
			 Queue payhment_success_queue = session.createQueue(sendName);
			 MessageProducer producer = session.createProducer(payhment_success_queue);
			 
			  	MapMessage mapMessage = new ActiveMQMapMessage();// hash结构
			  	if (map!=null) {
					for (String mkey : map.keySet()) {
						System.out.println(mkey+"//"+map.get(mkey));
						  mapMessage.setString(mkey,map.get(mkey)+"");//次数 订单号
					}
				}
			  	//大于0的话 设置延时队列 
			  	if (time>0) {
			  		mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,time);
				}

	            producer.send(mapMessage);

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

	
	
	@Override
	public int updatePay(PaymentInfo paymentInfo) {
		
		return paymentInfoMapper.updateInfo(paymentInfo);
	}



	
	@Override
	public PaymentInfo checkOrderStatus(String orderId) {
		PaymentInfo paymentInfo=new PaymentInfo();
		paymentInfo.setOrderId(orderId);
		
		
		return paymentInfoMapper.selectOne(paymentInfo);
	}



	

}
