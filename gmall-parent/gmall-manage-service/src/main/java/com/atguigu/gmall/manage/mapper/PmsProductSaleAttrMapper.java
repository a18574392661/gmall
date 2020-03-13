package com.atguigu.gmall.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.atguigu.gmall.bean.PmsProductSaleAttr;

import tk.mybatis.mapper.common.Mapper;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr>{

	@Insert("insert into pms_product_sale_attr values (null, #{productId},#{saleAttrId},#{saleAttrName})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
public int insertPmsProductSaleAttr(PmsProductSaleAttr pmsProductSaleAttr);
	
	
	//查询sku的属性选中 spu所有的属性 
	public List<PmsProductSaleAttr> queryPmsProductSaleAttrsSkus(@Param("skuid") String skuid,@Param("spuid") String spuid);

}
