package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.PmsSkuInfo;

public interface ItemSearchService {

	
	//接口同步数据 查询到索影库
	public List<PmsSkuInfo> queryAllSearchSku();
	
	//删除索影库
	public void delIndexSearchSku();
}
