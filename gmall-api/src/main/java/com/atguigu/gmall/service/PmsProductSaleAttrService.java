package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.PmsProductSaleAttr;

public interface PmsProductSaleAttrService {

	//��ѯspu���������� 
	List<PmsProductSaleAttr> querySpuId(String spuid,String skuid);
	
	
	
}
