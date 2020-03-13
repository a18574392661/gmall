package com.atguigu.gmall.manage.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.service.PmsBaseSaleAttrService;

@Controller
@CrossOrigin
public class PmsBaseSaleAttrController {
	
	@Reference
	private PmsBaseSaleAttrService pmsBaseSaleAttrService;
	
	@ResponseBody
	@RequestMapping("baseSaleAttrList")
	public List<PmsBaseSaleAttr> baseSaleAttrList(){
		
		
		return pmsBaseSaleAttrService.baseSaleAttrList();
	}


}
