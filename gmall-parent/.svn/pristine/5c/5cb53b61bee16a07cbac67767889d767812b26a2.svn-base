package com.atguigu.gmall.pay.mq;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.PaymentInfo;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.PayStatusMQ;
import com.atguigu.gmall.service.PaymentInfoService;
import com.atguigu.gmall.util.HttpclientUtil;

/*
 * 监听支付状态的 
 */
@Component
public class PayStatucMQinters {
	
	@Autowired
	private PayStatusMQ payStatusMQ;
	
	@Reference
	private PaymentInfoService paymentInfoService;
	

	//监听支付结果
	
	 @JmsListener(destination = "PAYSTATUSE",containerFactory =
	 "jmsQueueListener")
	 public void consumePaymentResultMapMessage(MapMessage mapMessage) throws JMSException { 
		 String out_trade_no =mapMessage.getString("out_trade_no"); 
		 String countString=mapMessage.getString("count");
		 //先检查幂等性的问题 同样的服务调用 返回结果一样 状态支付了去回调  去2次 
	PaymentInfo paymentInfo= paymentInfoService.checkOrderStatus(out_trade_no);
	if (paymentInfo!=null&&"已支付".equals(paymentInfo.getPaymentStatus())) {
		System.out.println("已经支付了");
		return ;
	}
	
		 int count=0;
		 if (countString!=null) {
			count=Integer.parseInt(countString);
		}
		 Map<String, Object> msMap=payStatusMQ.queryPayStatuc(out_trade_no, 5);//监听次数 避免死循环
		 System.out.println(countString);
		 count--;
		 if (count>0) {
			 if(msMap!=null&&msMap.get("sucess")==(Boolean)true) {
				 //代表支付成功了 去回调
				 System.out.println("去回调路径"); //幂等性的问题解决重复
				 Map<String,String> paramMap=new HashMap<String, String>();
					
					paramMap.put("trade_no", msMap.get("trade_no")+"");
					paramMap.put("trade_status",msMap.get("trade_status")+"");
					paramMap.put("total_amount",msMap.get("total_amount")+""); 
					paramMap.put("body",msMap.get("body")+"");
					paramMap.put("out_trade_no",out_trade_no);
					paramMap.put("callbackContent",msMap.get("callbackContent")+"");
					
				 HttpclientUtil.doPost("http://pay.gmall.com:8808/alipay/callback/return", paramMap);
				 
			 }else { //继续执行监听
				 System.out.println("继续监听"); 
				 
				 Map<String, Object> mqParemt=new HashMap<String, Object>();
					mqParemt.put("out_trade_no", out_trade_no);
					mqParemt.put("count", count);
					//创建延时队列
					paymentInfoService.sendTimeMessag("PAYSTATUSE", mqParemt, 3*1000);
					
			 }
		}else {
			System.out.println("监听次数完毕");
		}
		 
	 
	 }
	 
	
	
	
}
