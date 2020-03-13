package com.atguigu.gmall.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;

import com.atguigu.gmall.mapper.UsMapper;
import com.atguigu.gmall.mq.ActiveMQUtil;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.RedisUtil;

import io.netty.util.internal.StringUtil;
import redis.clients.jedis.Jedis;

import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.service.Uservice;

@Service
public class UserServiceimpl implements Uservice {

	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private UsMapper usmapper;
	
	@Autowired
	private ActiveMQUtil activeMQUtil;
	
	@Override
	public List<UmsMember> all() {
		String keyAll=Constants.UAll;
		Jedis jedis=null;
		List<UmsMember> list=null;
		try {
			jedis=redisUtil.getJedis();
			String vString=jedis.get(keyAll);
			if (StringUtils.isNotBlank(vString)) {
				list=JSON.parseArray(vString,UmsMember.class);
			}
			else {
				//数据库查询 放到缓存中 
				list=usmapper.selectAll();
				jedis.set(keyAll, JSON.toJSONString(list));				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		return list;
	}

	
	@Override
	public void add(UmsMember umsMember) {
		usmapper.insert(umsMember);
		Map msMap=new HashMap();
		msMap.put("id", umsMember.getId());
		sendTimeMessag("all",msMap,0);//查询所有接口
	}

	@Override
	public void del(String id) {
		usmapper.deleteByPrimaryKey(id);
		Map msMap=new HashMap();
		msMap.put("id",id);
		
		sendTimeMessag("all",msMap,0);//查询所有接口
	}

	@Override
	public void edit(UmsMember umsMember) {
		System.out.println(umsMember);
		usmapper.updateByPrimaryKey(umsMember);
		Map msMap=new HashMap();
		msMap.put("id",umsMember.getId());
		sendTimeMessag("all",msMap,0);//查询所有接口
	}

	@Override
	public UmsMember queryById(String id) {
		String keyId=Constants.UAll+":"+id;
		Jedis jedis=null;
		UmsMember u=null;
		try {
			jedis=redisUtil.getJedis();
			String vString=jedis.get(keyId);
			if (StringUtils.isNotBlank(vString)) {
				//u=JSON.parseObject(vString,UmsMember.class);
			}
			else {
				//数据库查询 放到缓存中 
				u=usmapper.selectByPrimaryKey(id);
				jedis.set(keyId, JSON.toJSONString(u));
				
			}
			//
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		return u;
	}

	
	//队列 每次查询增删改通知 删除缓存 重新分配
	public void sendTimeMessag(String sendName,Map<String, Object> map,int time) {
	
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
	public UmsMember login(UmsMember umsMember) {
			String key=umsMember.getUsername()+":"+umsMember.getPassword();
			Jedis jedis=redisUtil.getJedis();
			String u=jedis.get(key);
			UmsMember umsMember2=null;
			try {
				if (StringUtils.isNotBlank(u)) {
					umsMember2=JSON.parseObject(u, UmsMember.class);
					
				}else {
					//查询数据库到缓存拿 
					UmsMember user=new UmsMember();
					user.setUsername(umsMember.getUsername());
					user.setPassword(umsMember.getPassword());
					umsMember2=usmapper.selectOne(user);
					if (umsMember2!=null) {
						jedis.setex(key, 300000, JSON.toJSONString(umsMember2));
					}
					
					
					
				}
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally {
				if (jedis!=null) {
					jedis.close();
				}
			}
		
		return umsMember2;
	}



}
