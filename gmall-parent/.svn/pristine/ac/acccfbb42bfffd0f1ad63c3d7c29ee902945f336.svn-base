package com.atguigu.gmall.manage.service.lmpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.bean.PmsBaseCatalog2;
import com.atguigu.gmall.bean.PmsBaseCatalog3;
import com.atguigu.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import com.atguigu.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import com.atguigu.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import com.atguigu.gmall.service.CatalogService;

@Service
public class CatalogServicelmpl implements CatalogService {

	@Autowired
	PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;
	
	@Autowired
	PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;
	
	@Autowired
	PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;
	
	
	
	@Override
	public List<PmsBaseCatalog1> getCatalog1() {
	
		return pmsBaseCatalog1Mapper.selectAll();
	}

	@Override
	public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {
		PmsBaseCatalog2 pmsBaseCatalog2=new PmsBaseCatalog2();
		pmsBaseCatalog2.setCatalog1Id(catalog1Id);
		
		
		return pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
	}

	@Override
	public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
		// TODO Auto-generated method stub
		PmsBaseCatalog3 pmsBaseCatalog3=new PmsBaseCatalog3();
		pmsBaseCatalog3.setCatalog2Id(catalog2Id);
		return pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
	}

	
	@Override
	public List<PmsBaseCatalog1> queryAllCat() {
		// TODO Auto-generated method stub
		return pmsBaseCatalog1Mapper.queryAllCat();
	}

}
