package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.PmsSkuInfo;

public interface ItemSearchService {

	
	//�ӿ�ͬ������ ��ѯ����Ӱ��
	public List<PmsSkuInfo> queryAllSearchSku();
	
	//ɾ����Ӱ��
	public void delIndexSearchSku();
}
