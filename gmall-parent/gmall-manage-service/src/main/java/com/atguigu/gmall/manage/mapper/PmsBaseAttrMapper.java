package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;

import tk.mybatis.mapper.common.Mapper;

import javax.persistence.*;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @param
 * @return
 */
@org.apache.ibatis.annotations.Mapper
public interface PmsBaseAttrMapper extends Mapper<PmsBaseAttrInfo> {

	List<PmsBaseAttrInfo> queryAttrSku(@Param("pmsAttrs")String pmsAttrs);

 
}
