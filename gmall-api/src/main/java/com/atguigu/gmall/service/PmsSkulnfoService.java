package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuImage;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;

public interface PmsSkulnfoService {
	
	//spu查询sku
	List<PmsProductSaleAttr> spuSaleAttrList(String spuId);
	
	//spu的图片 
		List<PmsProductImage> spuImageList(String spuId);

		String saveSkuInfo(PmsSkuInfo pmsSkuInfo);
		
		//sku展示 
		PmsSkuInfo skuDeataInfoById(String skuid);

		List<PmsSkuInfo> querySkuAttrValues(String productId);
		
		void testRedission();
		
		//查询所有Sku
		List<PmsSkuInfo> queryAllSku();
	

}
