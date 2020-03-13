package com.atguigu.gmall.service;

import java.util.Map;

import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.PaymentInfo;

public interface PaymentInfoService {
	
	public int savePayinfo(PaymentInfo pInfo);

	public int updatePay(PaymentInfo paymentInfo);
	
	//¼àÌýÖ§¸¶×´Ì¬
	public Map<String, Object> queryPayStatuc(String orderId, int count);
	
	void sendTimeMessag(String sendName,Map<String, Object> map,int time);
	
	PaymentInfo checkOrderStatus(String orderId);
	

}
