package com.atguigu.gmall.order.mapper;

import org.apache.ibatis.annotations.Update;

import com.atguigu.gmall.bean.OmsOrder;

import tk.mybatis.mapper.common.Mapper;

public interface OrderMapper  extends Mapper<OmsOrder> {

	@Update("update oms_order set order_sn=#{orderSn},status=#{status} where id=#{id}")
	int updateOrderState(OmsOrder omsOrder);

}
