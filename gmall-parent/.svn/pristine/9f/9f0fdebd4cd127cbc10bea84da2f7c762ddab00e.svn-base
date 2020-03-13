package com.atguigu.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.user.mapper.UserMapper;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.RedisUtil;

import redis.clients.jedis.Jedis;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.validateMockitoUsage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Service
//用dubbo的
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Override
    public List<UmsMember> getAllUser() {

        List<UmsMember> umsMemberList = userMapper.selectAll();//userMapper.selectAllUser();

        return umsMemberList;
    }

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId) {
    	Jedis jedis=redisUtil.getJedis();
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses=new ArrayList<UmsMemberReceiveAddress>();
    	String userAddrKey=Constants.userAddrKey+memberId;
    	try {
		String vString=jedis.get(userAddrKey);
    	/*	if (StringUtils.isNotBlank(vString)) {
    			umsMemberReceiveAddresses=JSON.parseArray(vString, UmsMemberReceiveAddress.class);
    			
			}else {
				*/
				UmsMemberReceiveAddress umsMemberReceiveAddress = new UmsMemberReceiveAddress();
	    	        umsMemberReceiveAddress.setMemberId(memberId);
	    	         umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.select(umsMemberReceiveAddress);
	    	  //      jedis.set(userAddrKey, JSON.toJSONString(umsMemberReceiveAddress));
			//}
    		  
		} catch (Exception e) {
			e.printStackTrace();
		}
    	finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
    	
        // 封装的参数对象
     

    
//        Example example = new Example(UmsMemberReceiveAddress.class);
//        example.createCriteria().andEqualTo("memberId",memberId);
//        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = umsMemberReceiveAddressMapper.selectByExample(example);

        return umsMemberReceiveAddresses;
    }

	@Override
	public UmsMember getAllUserById(String memberId) {
		String key="user:"+memberId;
		Jedis jedis=redisUtil.getJedis();
		String valString=jedis.get(key);
		if (StringUtils.isNotBlank(valString)) {
			UmsMember member=JSON.parseObject(valString, UmsMember.class);
			return member;
		}
		else {
			 String token = UUID.randomUUID().toString();
			//分布式情况下设置锁
			String ok=jedis.set(key+":lock", token,"nx","px",1000*10);
			
			//如果拿到了锁 代表缓存没数据 
			if (StringUtils.isNotBlank(ok)&&ok.equals("OK")) {
				UmsMember umsMember=userMapper.selectByPrimaryKey(memberId);
				if (umsMember!=null) {
					jedis.set(key, JSON.toJSONString(umsMember));	
				}
				else {
					jedis.setex(key, 6000*3,JSON.toJSONString(""));//不存在的键用""代替	
				}
				
				  String lockToken = jedis.get(key+":lock");
				  if (StringUtils.isNotBlank(lockToken)&&lockToken.equals(token)) {
					  jedis.del(key+":lock");// 用token确认删除的是自己的sku的锁
				}
				jedis.close();
				return umsMember;
			}
			else {
				//自旋
				try {
					Thread.sleep(3000);
					return getAllUserById(memberId);
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
		
			
			
		}
		return null;
		
		
		
		
		
	}
	
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public void testRedis() {
		// TODO Auto-generated method stub
		System.out.println(redisUtil.getJedis());
	}

	@Override
	public List<UmsMember> userAll() {
		//缓存拿
	Jedis jedis=redisUtil.getJedis();
	String keyString="UserAll";
	String userVal=jedis.get(keyString);
	if (StringUtils.isNotBlank(userVal)) {
	List<UmsMember> list=JSON.parseArray(userVal, UmsMember.class);
	System.out.println("缓存拿");
		//返回缓存 
	return list;
	}
	//查询数据库 保存到 缓存 
	List<UmsMember> listUserList=userMapper.selectAll();
	jedis.setex(keyString, 6000*3, JSON.toJSONString(listUserList));
	System.out.println("数据库到缓存");
	return listUserList;
	}

	@Override
	public void delUserById(String memberId) {
		int c=userMapper.deleteByPrimaryKey(memberId);
		if (c>0) {
			Jedis jedis=redisUtil.getJedis();
			String key="user:"+memberId;
			String vaString=jedis.get(key);
			if (StringUtils.isNotBlank(vaString)) {
				jedis.del(vaString);
			}	
		}
		
		
	}

	
	@Override
	public UmsMember login(UmsMember umsMemberd) {
		Jedis jedis=redisUtil.getJedis();
		UmsMember loginUser=null;
		if (jedis==null) {
				return null;
		}
			
		try {
			
			//拿到缓存的数据 根据用户id生成 
			String userKey=Constants.UserKey+umsMemberd.getUsername()+":"+umsMemberd.getPassword();//根据用户名不可能重复 
			String userVal=jedis.get(userKey);
			if (StringUtils.isNotBlank(userVal)) {
				loginUser=JSON.parseObject(userVal,UmsMember.class);
				System.out.println("缓存里面的");
				return loginUser;
			}
			else {
				UmsMember yzMember=new UmsMember();
				yzMember.setUsername(umsMemberd.getUsername());
				//数据库查询 根据名字 
				UmsMember	loginUserSucess=userMapper.selectOne(yzMember);
				if (loginUserSucess!=null) {
					if (loginUserSucess.getPassword().equals(umsMemberd.getPassword())) {
						//登陆成功
						loginUser=loginUserSucess;
						//存在保存在redis
						jedis.setex(Constants.UserKey+umsMemberd.getUsername()+":"+umsMemberd.getPassword(), 60*30, JSON.toJSONString(loginUser));
						return loginUser;
					}
					else {
						//密码错误 
						System.out.println("密码错误");
					}
					
				}
				else {
					//用户名自不存在的 
					System.out.println("没有这个用户名自 ");
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
		//等于null没查到返回null
		return loginUser;
	}

	@Override
	public int saveUser(UmsMember umsMember) {
		
	int	c=userMapper.insert(umsMember);
		return c;
	}

	@Override
	public UmsMember getUser(String access_token) {
		UmsMember umsMember=new UmsMember();
		umsMember.setAccess_token(access_token);
		return userMapper.selectOne(umsMember);
	}

	@Override
	public int editUser(UmsMember umsMember) {
		// TODO Auto-generated method stub
		return userMapper.updateByPrimaryKey(umsMember);
	}

	@Override
	public UmsMemberReceiveAddress getReceiveAddressByMemberOrAddrId(String receiveAddressId) {
		
		return umsMemberReceiveAddressMapper.selectByPrimaryKey(receiveAddressId);
	}
	
	
}
