package com.atguigu.gmall.mq;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.PaymentInfo;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.PayStatusMQ;
import com.atguigu.gmall.service.PaymentInfoService;
import com.atguigu.gmall.service.Uservice;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.HttpclientUtil;
import com.atguigu.gmall.util.RedisUtil;

import io.netty.util.internal.StringUtil;
import redis.clients.jedis.Jedis;

/*
 * crud
 */
@Component
public class UserMQinters {
	
	@Reference
	Uservice uservice;
	
@Autowired
private RedisUtil redisUtil;
	



	 @JmsListener(destination = "all",containerFactory =
	 "jmsQueueListener")
	 public void consumePaymentResultMapMessage(MapMessage mapMessage) throws JMSException { 
	Jedis jedis=redisUtil.getJedis();
		 try {
			String allkey=Constants.UAll;
			jedis.del(allkey);
			uservice.all();//查询放到缓存 异步通知
			 System.out.println("入了队列");
			 
			 //增删改 删除 修改 新增 
			String id= mapMessage.getString("id");
			 if (StringUtils.isNotBlank(id)) {
				 jedis.del(allkey+":"+id);
				 
				 uservice.queryById(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		 finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		 uservice.all();
	 
	 }
	 
	
	
	
}
