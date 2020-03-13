package com.atguigu.gmall.manage.service.lmpl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsBaseCatalog1;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.CatalogService;
import com.atguigu.gmall.util.RedisUtil;

import redis.clients.jedis.Jedis;

@Service
public class AttrServicelmpl implements AttrService {

	@Autowired
	PmsBaseAttrMapper pmsAttrMapper;
	
	@Autowired
	PmsBaseAttrValueMapper pmsAttrValueMapper;
	
	
	@Override
	public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
		PmsBaseAttrInfo pmsBaseAttrInfo=new PmsBaseAttrInfo();
		pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
		List<PmsBaseAttrInfo>	pmsBaseAttrInfoList=pmsAttrMapper.select(pmsBaseAttrInfo);
		if (pmsBaseAttrInfoList!=null&&pmsBaseAttrInfoList.size()>0) {
			for (PmsBaseAttrInfo pmsBaseAttrInfo2 : pmsBaseAttrInfoList) {
				//查询属性的值
				PmsBaseAttrValue pmsBaseAttrValue=new PmsBaseAttrValue();
				pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo2.getId());
				List<PmsBaseAttrValue>	pmsBaseAttrValueList=pmsAttrValueMapper.select(pmsBaseAttrValue);
				pmsBaseAttrInfo2.setAttrValueList(pmsBaseAttrValueList);
			}
		}
		return pmsBaseAttrInfoList;
	}
	
	



	@Override
	public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
		int sum=0;
		int count=1;
		//判断属性id是否为null 如果是null批量添加 //不是则修改 删除 
		if (StringUtils.isBlank(pmsBaseAttrInfo.getId())) {
			
			 //添加属性表 
			int res=pmsAttrMapper.insert(pmsBaseAttrInfo);
			sum+=res;
			if (res>0) {
			 if (pmsBaseAttrInfo.getAttrValueList()!=null&&pmsBaseAttrInfo.getAttrValueList().size()>0) {
				//循环添加 
				 count+=pmsBaseAttrInfo.getAttrValueList().size();
				 for (PmsBaseAttrValue val : pmsBaseAttrInfo.getAttrValueList()) {
					//添加
					 val.setAttrId(pmsBaseAttrInfo.getId());
					sum+= pmsAttrValueMapper.insert(val);
				}
			}
			}
				
		}
		else {
			//修改 
			sum+=pmsAttrMapper.updateByPrimaryKey(pmsBaseAttrInfo);
			//批量删除
			//拿到数据库的输了 - 新的数量 (不搞了)
			//delete from 属性表 where cid=xxx and id not in （...）
		 pmsAttrValueMapper.deletePmsAttrInFoId(Integer.parseInt(pmsBaseAttrInfo.getId()));
		
		 //批量添加 
		 if (pmsBaseAttrInfo.getAttrValueList()!=null&&pmsBaseAttrInfo.getAttrValueList().size()>0) {
				//循环添加 
				 count+=pmsBaseAttrInfo.getAttrValueList().size();
				 for (PmsBaseAttrValue val : pmsBaseAttrInfo.getAttrValueList()) {
					//添加
					 val.setId(null);//重新添加 
					 val.setAttrId(pmsBaseAttrInfo.getId());
					 sum+= pmsAttrValueMapper.insert(val);
				}
			}
		 
		}
		
		

		//循环添加属性值表
		return sum>=count?"sucess":"error";
	}



	@Override
	public List<PmsBaseAttrInfo> queryAttrSku(Set<String> attrs) {
		
		if (attrs!=null) {
			int count=0;
			StringBuffer buffer=new StringBuffer("(");
			for (String aid : attrs) {
				buffer.append(aid);
				if (attrs.size()-1>count) {
					buffer.append(",");
				}
				count++;
			}
		
			//查询关联的平台属性 
			buffer.append(")");
			System.out.println(buffer.toString());
			List<PmsBaseAttrInfo>	pmsBaseAttrInfoList=pmsAttrMapper.queryAttrSku(buffer.toString());
			return pmsBaseAttrInfoList; 
		}
		return null; 
	}
	
	
	


}
