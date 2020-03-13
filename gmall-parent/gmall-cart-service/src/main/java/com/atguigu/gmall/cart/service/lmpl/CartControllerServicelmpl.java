package com.atguigu.gmall.cart.service.lmpl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.cart.mapper.OmsCartMapper;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.RedisUtil;

import redis.clients.jedis.Jedis;


@Service
@Transactional(propagation=Propagation.SUPPORTS)
public class CartControllerServicelmpl implements CartService {


	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private OmsCartMapper omsCartMapper;
	
	

	public OmsCartItem addUserCart(OmsCartItem omsCartItem, List<OmsCartItem> cookDbSku) {
		if (omsCartItem==null) {
			return null;
		}
		//用户id拼接key
		String userCartSkuKey=Constants.CacheSkuListName+omsCartItem.getMemberId();
		Jedis jedis=redisUtil.getJedis();
		//第一步先判断用户cache的key存不存在
		String userCartValue=jedis.get(userCartSkuKey);
		List<OmsCartItem> userOmsCartItemList=new ArrayList<OmsCartItem>();
		if (StringUtils.isNotBlank(userCartValue)) {
			//存在的话 拿到集合
			userOmsCartItemList=JSON.parseArray(userCartValue, OmsCartItem.class);
			//存在修改数量 数据库修改 数量 价格
			boolean ifesits=true;
			for (OmsCartItem omsCartItemUser : userOmsCartItemList) {
				if (omsCartItemUser.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
					//重复的 
					//数量添加
					omsCartItemUser.setQuantity(omsCartItemUser.getQuantity().add(omsCartItem.getQuantity()));
					//价格
					omsCartItemUser.setTotalPrice(omsCartItemUser.getQuantity().multiply(omsCartItemUser.getPrice()));
					//传过来的占时不变(根据需求定)
					ifesits=false;
					//修改数据库
					OmsCartItem omsCartItemUserDb=new OmsCartItem();
					omsCartItemUserDb.setMemberId(omsCartItem.getMemberId());
					omsCartItemUserDb.setProductSkuId(omsCartItem.getProductSkuId());
					omsCartItemUserDb.setQuantity(omsCartItemUser.getQuantity());
					omsCartItemUserDb.setTotalPrice(omsCartItemUser.getTotalPrice());
					omsCartMapper.editUserOmsCart(omsCartItemUserDb);
				}
			}
			if (ifesits) {
				//添加
				userOmsCartItemList.add(omsCartItem);
				omsCartMapper.insert(omsCartItem);
			}
			
			//不存在添加数据库 添加缓存 
		}else {
			//不存在  添加缓存 添加数据库  数据库一定没有这条数据
			userOmsCartItemList.add(omsCartItem);
			//数据库的数据就只跟redis同步算了 不跟DB同步 
			omsCartMapper.insert(omsCartItem);
		}
	
		//最后判断一下cookie的集合存不存在数据 循环添加到集合去重复添加数量 然后在保存到缓存
	/*	if (cookDbSku!=null&&cookDbSku.size()>0) {
			for (OmsCartItem cookSku : cookDbSku) {
				for (OmsCartItem userOmsCartItem : userOmsCartItemList) {
					//如果有重复的添加数量到缓存 

					if (cookSku.getProductSkuId().equals(userOmsCartItem.getProductSkuId())) {
						userOmsCartItem.setQuantity(cookSku.getQuantity().add(userOmsCartItem.getQuantity()));
						userOmsCartItem.setTotalPrice(userOmsCartItem.getQuantity().multiply(userOmsCartItem.getPrice()));
					
					}
					else {
						//不存在同步添加
						userOmsCartItemList.add(cookSku);
					}
				}
				
			}
			
		}*/
		
		//用户购物车数据永久不过期
		jedis.set(userCartSkuKey,JSON.toJSONString(userOmsCartItemList));
		return omsCartItem;
	}



	@Override
	public List<OmsCartItem> UserShopCart(String userId) {
		String userCartKey=Constants.CacheSkuListName+userId;
		Jedis jedis=redisUtil.getJedis();
		String userCarVal=jedis.get(userCartKey);
		List<OmsCartItem> omsCartItemList=new ArrayList<OmsCartItem>();
		if (StringUtils.isNotBlank(userCarVal)) {
			omsCartItemList=JSON.parseArray(userCarVal, OmsCartItem.class);
			
		}
		else {
			OmsCartItem omsCartItem=new OmsCartItem();
			omsCartItem.setMemberId(userId);
			omsCartItemList=omsCartMapper.select(omsCartItem);
			jedis.set(userCartKey, JSON.toJSONString(omsCartItemList));
		}
		return omsCartItemList;
	}



	@Override
	public int deleteOmsCart(String memberId) {
		//删除购物车
		Jedis jedis=redisUtil.getJedis();
		String userCartKey=Constants.CacheSkuListName+memberId;
		try {
		 jedis.del(userCartKey);
			//数据库删除
		 OmsCartItem omsCartItem=new OmsCartItem();
		 omsCartItem.setMemberId(memberId);
		 omsCartMapper.delete(omsCartItem);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		return 1;
	}

}
