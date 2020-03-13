package com.atguigu.gmall.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.manage.service.lmpl.PmsSkulnfoServicelmpl;

import tk.mybatis.mapper.common.Mapper;

public interface PmsSkulnfoMapper extends Mapper<PmsSkuInfo>{

	List<PmsSkuInfo> selectSkuSaleAttrValueListBySpu(@Param("productId")String productId);
}
