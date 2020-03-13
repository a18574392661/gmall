package com.atguigu.gmall.service;

import java.util.Map;

public interface PayStatusMQ {

	public Map<String, Object> queryPayStatuc(String orderId,int count);
}
