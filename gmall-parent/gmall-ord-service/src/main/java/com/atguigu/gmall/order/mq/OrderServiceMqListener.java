package com.atguigu.gmall.order.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.service.OrderService;

@Component
public class OrderServiceMqListener {

	
	@Autowired
	private OrderService orderService;
	
	//修改完日志状态 订阅通知修改订单状态
	 @JmsListener(destination = "PAYHMENT_SUCCESS_QUEUE",containerFactory = "jmsQueueListener")
	public void consumePaymentResultMapMessage(MapMessage mapMessage) throws JMSException {
		  String out_trade_no = mapMessage.getString("out_trade_no");
		  String pay_no = mapMessage.getString("trade_no");
		  
		  OmsOrder omsOrder=new OmsOrder();
		  omsOrder.setId(out_trade_no);
		  omsOrder.setOrderSn(pay_no);
		  omsOrder.setStatus(1);
		  //跟新订单信息 
		  System.out.println(out_trade_no+"//"+pay_no);
		  orderService.updateOrderSta(omsOrder);
	}
}
