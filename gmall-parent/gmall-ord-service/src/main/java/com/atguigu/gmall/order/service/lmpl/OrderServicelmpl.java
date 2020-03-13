package com.atguigu.gmall.order.service.lmpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.OmsOrderItem;
import com.atguigu.gmall.bean.PaymentInfo;
import com.atguigu.gmall.mq.ActiveMQUtil;
import com.atguigu.gmall.order.mapper.OrderItemMapper;
import com.atguigu.gmall.order.mapper.OrderMapper;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.PaymentInfoService;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.RedisUtil;

import redis.clients.jedis.Jedis;

@Service
public class OrderServicelmpl implements OrderService {

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	
	@Reference
	private PaymentInfoService paymentInfoService;
	
	
	@Override
	public String createOrderMath(String uid) {
		Jedis jedis=redisUtil.getJedis();
		String maths="";
		try {
			String orderKey=Constants.OrderKey+uid;
			
		String vString=jedis.get(orderKey);
			if (StringUtils.isNotBlank(vString)) {
				maths=vString;
			}
			else {
				jedis.setex(orderKey, 60*15, uid+UUID.randomUUID());//15分钟过期 
				maths=jedis.get(orderKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		return maths;
	}

	@Override
	public Boolean checkedOrderCode(String memberId, String tradeCode) {
		Jedis jedis=redisUtil.getJedis();
		boolean b=false;
		try {
			String userOrderCodeKey=Constants.OrderKey+memberId;
			String code=jedis.get(userOrderCodeKey);
			System.out.println(code);
			System.out.println(tradeCode);
			
		    String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
	           Long eval = (Long) jedis.eval(script, Collections.singletonList(userOrderCodeKey), Collections.singletonList(tradeCode));
	           
			if (eval!=null&&eval!=0) {
				//是惟一的 删除 这个键盘 代表第一次提交这个订单
				jedis.del(userOrderCodeKey);
				b=true;
			}
			
		} finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		
		
		return b;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int saveOrderItem(OmsOrder omsOrder) {
		int sucees=1;
		try {
			List<OmsOrderItem> items=omsOrder.getOmsOrderItems();
			int c=orderMapper.insert(omsOrder);
			if (c>0) {
				//添加明细表 
				for (OmsOrderItem omsOrderItem : items) {
					omsOrderItem.setOrderId(omsOrder.getId());
					orderItemMapper.insert(omsOrderItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sucees=0;
		}
		finally {
			
		}
		
		return sucees;
	}

	@Override
	public List<OmsOrder> queryUserOrder(String memberId) {
		//查询该用户的所有订单 永不过期占时
		Jedis jedis=redisUtil.getJedis();
		String userOrderKey=Constants.UserOrderKey+memberId;
		List<OmsOrder> omsOrders=new ArrayList<OmsOrder>();
		try {
		String userOrderVal=jedis.get(userOrderKey);
		if (StringUtils.isNotBlank(userOrderVal)) {
			omsOrders=JSON.parseArray(userOrderVal, OmsOrder.class);
		}
		else {
			OmsOrder omsOrder=new OmsOrder();
			omsOrder.setMemberId(memberId);
			//查询驾到缓存 
			//这里又涉及到分布式锁
			omsOrders=orderMapper.select(omsOrder);
			System.out.println(omsOrder);
			if (omsOrders!=null&&omsOrders.size()>0) {
				jedis.set(userOrderKey, JSON.toJSONString(omsOrders));
				
			}
			else {
				jedis.set(userOrderKey,"");//保存null值防止攻击
			}
			
		}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		return omsOrders;
	}

	@Override
	public OmsOrder queryUserOrderByid(String orderCode) {
		//查询订单详细信息
		Jedis jedis=redisUtil.getJedis();
		String OrderKeyByid=Constants.OrderKeyByid+orderCode;
		OmsOrder omsOrder=new OmsOrder();
		try {
			String orderId=jedis.get(OrderKeyByid);
			if (StringUtils.isNotBlank(orderId)) {
				omsOrder=JSON.parseObject(orderId, OmsOrder.class);
			}
			else {
				//查询订单详情
				omsOrder=orderMapper.selectByPrimaryKey(orderCode);
				if (omsOrder!=null) {
					//查询详细信息
					OmsOrderItem omsOrderItem=new OmsOrderItem();
					omsOrderItem.setOrderId(omsOrder.getId());
					omsOrder.setOmsOrderItems(orderItemMapper.select(omsOrderItem));
					jedis.set(OrderKeyByid, JSON.toJSONString(omsOrder));
					
				}else {
					//缓存穿透 
					jedis.setex(OrderKeyByid, 60*3, "");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		return omsOrder;
	}

	@Autowired
	private ActiveMQUtil activeMQUtil;
	
	@Override
	public int updateOrdePayInforStatus(Map msMap) {
		String orderCode=(String)msMap.get("out_trade_no");
		String trade_no=(String)msMap.get("trade_no");
		
		String callbackContent=(String)msMap.get("callbackContent");
		
		/*
		 * Jedis jedis=redisUtil.getJedis(); String
		 * OrderKeyByid=Constants.OrderKeyByid+orderCode;
		 */
		//修改订单状态 redis同步 
		try {
			/*	OmsOrder omsOrder=new OmsOrder();
			omsOrder.setId(orderCode);
			omsOrder.setStatus(1);//代发货 
			omsOrder.setOrderSn(trade_no);
			int count=orderMapper.updateOrderState(omsOrder);*/
			//if (count>0) {
				//获得这个订单同步缓存 
				/*String orderId=jedis.get(OrderKeyByid);
				if (StringUtils.isNotBlank(orderId)) {
				OmsOrder omsOrderCacth=JSON.parseObject(orderId, OmsOrder.class);
				omsOrderCacth.setStatus(1);
				jedis.set(OrderKeyByid, JSON.toJSONString(OrderKeyByid));
				}
				*/
				//生成一个消息 订单去订阅
				
				
				//修改日志表
				PaymentInfo paymentInfo=new PaymentInfo();
				paymentInfo.setAlipayTradeNo(trade_no);
				paymentInfo.setOrderId(orderCode);
				paymentInfo.setPaymentStatus("已支付");;
				paymentInfo.setCallbackTime(new Date());
				paymentInfo.setCallbackContent(callbackContent);
				paymentInfoService.updatePay(paymentInfo);	
				Map map=new HashMap();
				map.put("out_trade_no", orderCode);
				
				map.put("trade_no", trade_no);
				//添加到队列
				sendMessag("PAYHMENT_SUCCESS_QUEUE",map);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			
		}
		
		//修改日志信息
		return 0;
	}
	
	public void sendMessag(String sendName,Map<String, Object> map) {
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
						
						  mapMessage.setString(mkey,map.get(mkey)+"");
					}
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
	public int updateOrderSta(OmsOrder omsOrder) {
		orderMapper.updateOrderState(omsOrder);
		return 0;
	}

}
