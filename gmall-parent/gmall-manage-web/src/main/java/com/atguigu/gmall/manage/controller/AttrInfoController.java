package com.atguigu.gmall.manage.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.CatalogService;

/*
 * 商品参数 一个分类多个参数
 */
@Controller
@CrossOrigin // 解决跨域问题
public class AttrInfoController {
	
	@Reference
	private AttrService attrService;

	
	@ResponseBody
	@RequestMapping("attrInfoList")
	public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){
		
		
		return attrService.attrInfoList(catalog3Id);
	}
	
	@ResponseBody
	@RequestMapping("saveAttrInfo")
	public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
		attrService.saveAttrInfo(pmsBaseAttrInfo);
		return "sucess";
	}
	
	/*
	 * public static void main(String[] args) { String b=null; String c="";
	 * 
	 * System.out.println(!StringUtils.isEmpty(b)+"//"+!StringUtils.isEmpty(c)); }
	 */
}
