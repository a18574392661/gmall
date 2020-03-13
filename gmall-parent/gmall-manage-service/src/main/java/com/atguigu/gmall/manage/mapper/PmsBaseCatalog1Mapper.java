package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.bean.PmsBaseCatalog2;
import com.atguigu.gmall.bean.UmsMember;

import tk.mybatis.mapper.common.Mapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @param
 * @return
 */

public interface PmsBaseCatalog1Mapper extends  Mapper<PmsBaseCatalog1> {

	List<PmsBaseCatalog1> queryAllCat();
   
}

