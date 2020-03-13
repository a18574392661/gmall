package com.atguigu.gmall.manage.controller;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.PmsSkulnfoService;

@Controller
@CrossOrigin
public class PmsSkulnfoController {

	@Reference
	private PmsSkulnfoService pmsSkulnfoService;
	
	@ResponseBody
	@RequestMapping("spuSaleAttrList")
	public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){
		
		return pmsSkulnfoService.spuSaleAttrList(spuId);
	}
	
	@ResponseBody
	@RequestMapping("spuImageList")
	public List<PmsProductImage> spuImageList(String spuId){
		
		return pmsSkulnfoService.spuImageList(spuId);
	}
	
	/*
	 * 添加sku表
	 */
	@ResponseBody
	@RequestMapping("saveSkuInfo")
	public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo) {
		
		
		return pmsSkulnfoService.saveSkuInfo(pmsSkuInfo);
	}
}
