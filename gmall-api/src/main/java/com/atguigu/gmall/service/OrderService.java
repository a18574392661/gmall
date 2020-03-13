package com.atguigu.gmall.service;

import java.util.List;
import java.util.Map;

import com.atguigu.gmall.bean.OmsOrder;

public interface OrderService {
	
	//获取防止订单重复吗
	String createOrderMath(String uid);
	Boolean checkedOrderCode(String memberId,String tradeCode);
	int saveOrderItem(OmsOrder omsOrder);

	List<OmsOrder> queryUserOrder(String memberId);
	OmsOrder queryUserOrderByid(String orderCode);
	int updateOrdePayInforStatus(Map msMap);
	
	//修改订单状态
	int updateOrderSta(OmsOrder omsOrder);
	
}
