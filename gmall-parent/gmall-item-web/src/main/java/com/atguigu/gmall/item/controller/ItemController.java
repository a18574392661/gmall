package com.atguigu.gmall.item.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.CatalogService;
import com.atguigu.gmall.service.PmsBaseSaleAttrService;
import com.atguigu.gmall.service.PmsProductSaleAttrService;
import com.atguigu.gmall.service.PmsSkulnfoService;

/*
 * 商品参数 一个分类多个参数
 */
@Controller
@CrossOrigin // 解决跨域问题
public class ItemController {
	
	@Reference
	private PmsSkulnfoService pmsSkulnfoService;


	
	//销售属性
	@Reference
	private PmsProductSaleAttrService pmsProductSaleAttrService;
	
	@RequestMapping("q")
	public String skuDeatailAllById(String skuId,ModelMap map) {
		PmsSkuInfo pmsSkuInfo=pmsSkulnfoService.skuDeataInfoById(skuId);
		map.put("skuInfo", pmsSkuInfo);
		if (pmsSkuInfo!=null) {
			//防止击穿
			//查询sku的销售属性 属性值 有哪些 
			//sku的spuid去查询 他的属性跟纸
			List<PmsProductSaleAttr> pmsProductSaleAttrList=pmsProductSaleAttrService.querySpuId(pmsSkuInfo.getProductId(),skuId);
			map.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrList);
			
			
			//查询当前spu下面所欲sku的属性值
			//便利把每个sku的值对应 k拼接当前sku的值
			Map<String, String> returnMap=new HashMap<String, String>();
			List<PmsSkuInfo>	pmsSkuInfoList=pmsSkulnfoService.querySkuAttrValues(pmsSkuInfo.getProductId());
			if (pmsSkuInfoList!=null&&pmsSkuInfoList.size()>0) {
				for (PmsSkuInfo info : pmsSkuInfoList) {
					String k="";
				List<PmsSkuSaleAttrValue>	PmsSkuSaleAttrValueList=info.getSkuSaleAttrValueList();
				if (PmsSkuSaleAttrValueList!=null&&PmsSkuSaleAttrValueList.size()>0) {
					int i=0;
					for (PmsSkuSaleAttrValue val : PmsSkuSaleAttrValueList) {
						
						k+=val.getSaleAttrValueId();
						if (PmsSkuSaleAttrValueList.size()-1>i) {
						k+="|";
						}
						i++;
					}
					returnMap.put(k,info.getId());
				}
					
				}
			}
			String skuSaleAttrHashJsonStr=JSON.toJSONString(returnMap);
			System.out.println(skuSaleAttrHashJsonStr);
			map.put("skuSaleAttrHashJsonStr", skuSaleAttrHashJsonStr);
		}
	
		
		return "item";
	}

	
	
	@RequestMapping("test")
	@ResponseBody
	public void testRedison() {
		//测试redission分布式锁 
		pmsSkulnfoService.testRedission();
	}
	
}
