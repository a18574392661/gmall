package com.atguigu.gmall.manage.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.AttrValueService;
import com.atguigu.gmall.service.CatalogService;

/*
 * 商品参数 一个分类多个参数
 */
@Controller
@CrossOrigin // 解决跨域问题
public class AttrInfoValueController {
	
	@Reference
	private AttrValueService attrService;

	
	@ResponseBody
	@RequestMapping("getAttrValueList")
	public List<PmsBaseAttrValue> getAttrValueList(String attrId){
		
		
		return attrService.getAttrValueList(attrId);
	}
	

	
}
