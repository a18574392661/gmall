package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.OmsCartItem;

public interface CartService {
	//�ȷ�����ӹ��ﳵ��ĳ������ �����ȸ���  cookie��ֵͬ��
	public OmsCartItem addUserCart(OmsCartItem omsCartItem,List<OmsCartItem> cookDbSku);

	public List<OmsCartItem> UserShopCart(String userId);

	public int deleteOmsCart(String memberId);
}
