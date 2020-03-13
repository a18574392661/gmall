package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.PmsProductInfo;

public interface PmsProductInfoService {

	List<PmsProductInfo> spuList(String catalog3Id);

	String saveSpuInfo(PmsProductInfo pmsProductInfo);
	
}
