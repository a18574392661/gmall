package com.atguigu.gmall.manage.controller;

import static org.mockito.Mockito.RETURNS_SMART_NULLS;

import java.io.IOException;
import java.util.List;

import org.csource.common.MyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.service.PmsProductInfoService;
import com.atguigu.gmall.util.PmsUploadUtil;

/*
 * 商品主表 
 */
@Controller
@CrossOrigin // 解决跨域问题
public class PmsProductInfoController {

	@Reference
	private PmsProductInfoService pmsProductInfoService;
	
	@ResponseBody
	@RequestMapping("/spuList")
	public List<PmsProductInfo> spuList(String catalog3Id){
		
		
		return pmsProductInfoService.spuList(catalog3Id);
		
	}
	
	
	/*
	 * 添加spu表(商品主表) 销售属性表 属性值表 
	 */
	@RequestMapping("saveSpuInfo")
	@ResponseBody
	public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
		
		
		
		return pmsProductInfoService.saveSpuInfo(pmsProductInfo);
	}
	
	/*
	 * 图片上传到服务器 
	 */
	@ResponseBody
	@RequestMapping("fileUpload")
	public String fileUpload(@RequestParam("file") MultipartFile multipartFile)   {
		//图片上传到服务器 名字回显过去src 隐藏域 
	 try {
		String imgUrl =PmsUploadUtil.uploadImage(multipartFile);
		//替换原来的路径
		imgUrl = imgUrl.replace("group1", "");
		imgUrl = imgUrl.replace("M00", "");
		return imgUrl;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		
		return null;
	}
	
	
}
