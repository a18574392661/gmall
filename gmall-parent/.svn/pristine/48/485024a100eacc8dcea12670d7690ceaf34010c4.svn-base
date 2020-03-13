package com.atguigu.gmall.manage.service.lmpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkulnfoMapper;
import com.atguigu.gmall.service.ItemSearchService;

@Service
public class ItemSearchServicelmpl implements ItemSearchService {

	@Autowired
	private PmsSkulnfoMapper pmsSkulnfoMapper;
	
	@Autowired
	private PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
	
	
	
	@Override
	public List<PmsSkuInfo> queryAllSearchSku() {
		// TODO Auto-generated method stub
		List<PmsSkuInfo> 	pmsSkuInfoList=pmsSkulnfoMapper.selectAll();
		//查询它的销售属性 
		for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
			PmsSkuAttrValue pmsValue=new PmsSkuAttrValue();
			pmsValue.setSkuId(pmsSkuInfo.getId());
			
			List<PmsSkuAttrValue> pmsSkuAttrValueList=pmsSkuAttrValueMapper.select(pmsValue);
		
			pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValueList);
		}
		return pmsSkuInfoList;
	}
	
	
	
	@Override
	public void delIndexSearchSku() {
	

	}
	
	

}
