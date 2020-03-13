package com.atguigu.gmall.manage.service.lmpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.service.AttrValueService;

@Service
public class AttrValueServicelmpl implements AttrValueService {

	@Autowired
	private PmsBaseAttrValueMapper pmsBaseAttrValueMapper;
	
	
	public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
		// TODO Auto-generated method stub
		PmsBaseAttrValue pmsBaseAttrValue=new PmsBaseAttrValue();
		pmsBaseAttrValue.setAttrId(attrId);
		return pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
	}

}
