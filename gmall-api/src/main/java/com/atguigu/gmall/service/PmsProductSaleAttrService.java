package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.PmsProductSaleAttr;

public interface PmsProductSaleAttrService {

	//查询spu的销售属性 
	List<PmsProductSaleAttr> querySpuId(String spuid,String skuid);
	
	
	
}
