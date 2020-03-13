package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuImage;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;

public interface PmsSkulnfoService {
	
	//spu��ѯsku
	List<PmsProductSaleAttr> spuSaleAttrList(String spuId);
	
	//spu��ͼƬ 
		List<PmsProductImage> spuImageList(String spuId);

		String saveSkuInfo(PmsSkuInfo pmsSkuInfo);
		
		//skuչʾ 
		PmsSkuInfo skuDeataInfoById(String skuid);

		List<PmsSkuInfo> querySkuAttrValues(String productId);
		
		void testRedission();
		
		//��ѯ����Sku
		List<PmsSkuInfo> queryAllSku();
	

}
