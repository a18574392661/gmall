package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.OmsCartItem;

public interface CartService {
	//先返回添加购物车的某个对象 数量先更改  cookie的值同步
	public OmsCartItem addUserCart(OmsCartItem omsCartItem,List<OmsCartItem> cookDbSku);

	public List<OmsCartItem> UserShopCart(String userId);

	public int deleteOmsCart(String memberId);
}
