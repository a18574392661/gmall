package com.atguigu.gmall.manage.service.lmpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.atguigu.gmall.service.PmsBaseSaleAttrService;

@Service
public class PmsBaseSaleAttrServicelmpl implements PmsBaseSaleAttrService {

	@Autowired
	private PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;
	
	
	
	
	public List<PmsBaseSaleAttr> baseSaleAttrList() {
		// TODO Auto-generated method stub
		return pmsBaseSaleAttrMapper.selectAll();
	}



}
