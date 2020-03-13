package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.PmsBaseAttrValue;

public interface AttrValueService {

	List<PmsBaseAttrValue> getAttrValueList(String attrId);
}
