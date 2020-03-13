package com.atguigu.gmall.cart.mapper;

import com.atguigu.gmall.bean.OmsCartItem;

import tk.mybatis.mapper.common.Mapper;


public interface OmsCartMapper extends Mapper<OmsCartItem> {

	int editUserOmsCart(OmsCartItem omsCartItemUserDb);
	

}
