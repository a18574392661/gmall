package com.atguigu.gmall.manage.service.lmpl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsProductSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsProductImageMapper;
import com.atguigu.gmall.manage.mapper.PmsProductInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.atguigu.gmall.service.PmsProductInfoService;

@Service
@Transactional(propagation=Propagation.REQUIRED)
public class PmsProductInfoServicelmpl implements PmsProductInfoService {

	
	@Autowired
	private PmsProductInfoMapper pmsProductInfoMapper;
	@Autowired
	private PmsProductImageMapper pmsProductImageMapper;
	
	@Autowired
	private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
	
	@Autowired
	private PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
	
	@Override
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<PmsProductInfo> spuList(String catalog3Id) {
		// TODO Auto-generated method stub
		PmsProductInfo pmsProductInfo=new PmsProductInfo();
		pmsProductInfo.setCatalog3Id(catalog3Id);
		return pmsProductInfoMapper.select(pmsProductInfo);
	}

	@Override
	public String saveSpuInfo(PmsProductInfo pmsProductInfo) {
		int count=1;//修改或者添加spu
		int sum=0;
		sum+=pmsProductInfoMapper.insert(pmsProductInfo);
		//获得图片集合
		List<PmsProductImage> pmsProductImagesList=pmsProductInfo.getSpuImageList();
		if (pmsProductImagesList!=null&&pmsProductImagesList.size()>0) {
			count+=pmsProductImagesList.size();
			for (PmsProductImage pmsProductImage : pmsProductImagesList) {
					pmsProductImage.setProductId(pmsProductInfo.getId());
			        sum+=pmsProductImageMapper.insert(pmsProductImage);
			}
			
		}
		
		//获取销售属性
		List<PmsProductSaleAttr> pmsProductSaleAttrList=pmsProductInfo.getSpuSaleAttrList();
		
		if (pmsProductSaleAttrList!=null&&pmsProductSaleAttrList.size()>0) {
			count+=pmsProductSaleAttrList.size();
			for (PmsProductSaleAttr pmsProductSaleAttr : pmsProductSaleAttrList) {
				//添加每个spu的销售属性值
				pmsProductSaleAttr.setProductId(pmsProductInfo.getId());
				sum+=pmsProductSaleAttrMapper.insertPmsProductSaleAttr(pmsProductSaleAttr);
				
				//销售属性值 
				List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList=pmsProductSaleAttr.getSpuSaleAttrValueList();
				if (pmsProductSaleAttrValueList!=null&&pmsProductSaleAttrValueList.size()>0) {
					count+=pmsProductSaleAttrValueList.size();
					for (PmsProductSaleAttrValue pmsProductSaleAttrValue : pmsProductSaleAttrValueList) {
						pmsProductSaleAttrValue.setProductId(pmsProductInfo.getId());
					//	pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr.getId());
					    pmsProductSaleAttrValueMapper.insert(pmsProductSaleAttrValue);
					
					}
					
				}
			}
		}
		
		
		return sum>=count?"scess":"error";
	}

}
