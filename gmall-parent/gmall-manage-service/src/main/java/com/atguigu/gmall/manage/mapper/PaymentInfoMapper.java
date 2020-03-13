package com.atguigu.gmall.manage.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.atguigu.gmall.bean.PaymentInfo;

import tk.mybatis.mapper.common.Mapper;

public interface PaymentInfoMapper extends Mapper<PaymentInfo>{

	@Update("update payment_info set alipay_trade_no=#{alipayTradeNo},payment_status=#{paymentStatus},callback_content=#{callbackContent},callback_time=now() where order_id=#{orderId}")
	int updateInfo(PaymentInfo paymentInfo);

	
	@Select("select * from payment_info where order_id=#{orderId}")
	PaymentInfo selectOneDisbale(@Param("orderId") String orderId);

}
