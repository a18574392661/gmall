package com.atguigu.gmall.manage.service.lmpl;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.RedisUtil;

import redis.clients.jedis.Jedis;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsProductSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.atguigu.gmall.service.PmsProductSaleAttrService;

@Service
@Transactional(propagation=Propagation.REQUIRED)
public class PmsProductSaleAttrServicelmpl implements PmsProductSaleAttrService {

	@Autowired
	private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
	
	@Autowired
	private PmsProductSaleAttrValueMapper PmsProductSaleAttrValueMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public List<PmsProductSaleAttr> querySpuId(String spuid,String skuid) {
		/*
		 * PmsProductSaleAttr productSaleAttr1=new PmsProductSaleAttr();
		 * productSaleAttr1.setProductId(spuid); List<PmsProductSaleAttr>
		 * productSaleAttrList=pmsProductSaleAttrMapper.select(productSaleAttr1); if
		 * (productSaleAttrList!=null&&productSaleAttrList.size()>0) { for
		 * (PmsProductSaleAttr pmsProductSaleAttr : productSaleAttrList) {
		 * PmsProductSaleAttrValue pmsProductSaleAttrValue=new
		 * PmsProductSaleAttrValue(); pmsProductSaleAttrValue.setProductId(spuid);
		 * pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr.getSaleAttrId());
		 * List<PmsProductSaleAttrValue>
		 * pmsProductSaleAttrValueList=PmsProductSaleAttrValueMapper.select(
		 * pmsProductSaleAttrValue);
		 * pmsProductSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValueList); } }
		 * //return productSaleAttrList;
		 */
		/*
		List<PmsProductSaleAttr> pmsProductSaleAttrList=null;
		String skuAttrValueKey=Constants.Spu+spuid+":"+Constants.Sku+skuid+Constants.AttrAndValue;
		System.out.println(skuAttrValueKey);
		Jedis jedis=redisUtil.getJedis();
		try {
			String spuSkuAttrVal=jedis.get(skuAttrValueKey);
			if (StringUtils.isNotBlank(spuSkuAttrVal)) {
				//存在
				pmsProductSaleAttrList=JSON.parseArray(spuSkuAttrVal, PmsProductSaleAttr.class);
			}
			else {
				 String token = UUID.randomUUID().toString();
					//防止击穿问题 缓存键刚好过期了 没访问到什么的 
					//设置redis自带的分布式锁 
					String ok=jedis.set(Constants.Spu+spuid+":"+Constants.Sku+skuid+Constants.AttrAndValue+":lock", token,"nx","px",1000*10);
				//不存在 多线程高并发情况下 先开锁
					System.out.println(StringUtils.isNotBlank(ok)+"//"+ok.equals("OK"));
				if (StringUtils.isNotBlank(ok)&&ok.equals("Ok")) {
					//拿到了锁 第一次 一定可以拿到
				pmsProductSaleAttrList=pmsProductSaleAttrMapper.queryPmsProductSaleAttrsSkus(skuid, spuid);
				//以防null值
				if (pmsProductSaleAttrList!=null) {
					jedis.set(skuAttrValueKey, JSON.toJSONString(pmsProductSaleAttrList));
				}
				else {
					jedis.setex(skuAttrValueKey,60*3 ,JSON.toJSONString(""));
				}
				
				//判断存不存在锁 关闭
				String lock=jedis.get(skuAttrValueKey+":lock");
				if (StringUtils.isNotBlank(lock)&&lock.equals(token)) {
					//自己的 
					jedis.del(lock);
				}
				
				}
				else {
					try {
						//等待释放 
						Thread.sleep(Constants.ReditDeTime);
						pmsProductSaleAttrList=querySpuId(spuid,skuid);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (jedis!=null) {
				jedis.close();
			}
		}
		*/
		//根据spuId跟Skuid
	
		
		return pmsProductSaleAttrMapper.queryPmsProductSaleAttrsSkus(skuid, spuid);// pmsProductSaleAttrList;// 
	}

}
