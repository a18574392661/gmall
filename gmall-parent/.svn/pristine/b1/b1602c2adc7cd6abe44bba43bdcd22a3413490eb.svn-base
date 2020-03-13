package com.atguigu.gmall.manage.service.lmpl;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.bean.PmsProductSaleAttrValue;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuImage;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsProductImageMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.atguigu.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuImageMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkulnfoMapper;
import com.atguigu.gmall.service.PmsSkulnfoService;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.GmallRedissonConfig;
import com.atguigu.gmall.util.RedisUtil;

import redis.clients.jedis.Jedis;

@Service
@Transactional(propagation=Propagation.REQUIRED)
public class PmsSkulnfoServicelmpl implements PmsSkulnfoService {

	
	@Autowired
	private PmsSkulnfoMapper pmsSkulnfoMapper;
	@Autowired
	private PmsSkuImageMapper pmsSkuImageMapper;
	
	@Autowired
	private PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
	
	@Autowired
	private PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
	
	//spu的图片 
	@Autowired
	private PmsProductImageMapper pmsProductImageMapper;
	
	@Autowired
	private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
	
	@Autowired
	private PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
	
	
	@Autowired
	RedisUtil redisUtil;
	

	
	@Override
	public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
	 //查询spu的所有销售属性
		PmsProductSaleAttr pmsProductSaleAttr=new PmsProductSaleAttr();
		pmsProductSaleAttr.setProductId(spuId);
	    List<PmsProductSaleAttr>	pmsProductSaleAttrList=pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
		if (pmsProductSaleAttrList!=null&&pmsProductSaleAttrList.size()>0) {
			for (PmsProductSaleAttr pmsProductSaleAttr2 : pmsProductSaleAttrList) {
				PmsProductSaleAttrValue pmsProductSaleAttValue=new PmsProductSaleAttrValue();
				pmsProductSaleAttValue.setSaleAttrId(pmsProductSaleAttr2.getId());
				List<PmsProductSaleAttrValue> pmsProductSaleAttrValueList=pmsProductSaleAttrValueMapper.select(pmsProductSaleAttValue);
				//添加每个销售属性的值
				pmsProductSaleAttr2.setSpuSaleAttrValueList(pmsProductSaleAttrValueList);
			} 
		}
	return pmsProductSaleAttrList;
		
	}

	@Override
	public List<PmsProductImage> spuImageList(String spuId) {
	PmsProductImage productImage=new PmsProductImage();
	productImage.setProductId(spuId);
	
		return pmsProductImageMapper.select(productImage);
	}

	@Override
	public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
		pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
			 int count=1;
			 int sum=0;
			 sum+=pmsSkulnfoMapper.insert(pmsSkuInfo);
	List<PmsSkuImage>	pmsSkuImageList=pmsSkuInfo.getSkuImageList();
	if (pmsSkuImageList!=null&&pmsSkuImageList.size()>0) {
		//批量添加图片 
		count+=pmsSkuImageList.size();
		for (PmsSkuImage pmsSkuImage : pmsSkuImageList) {
			pmsSkuImage.setSkuId(pmsSkuInfo.getId());
			sum+=pmsSkuImageMapper.insert(pmsSkuImage);
		}
	}
	
	//获取sku的多个属性
	List<PmsSkuAttrValue> pmsSkuAttrValueList=pmsSkuInfo.getSkuAttrValueList();
	if (pmsSkuAttrValueList!=null&&pmsSkuAttrValueList.size()>0) {
			count+=pmsSkuAttrValueList.size();
			for (PmsSkuAttrValue pmsSkuAttrValue : pmsSkuAttrValueList) {
				pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
				pmsSkuAttrValueMapper.insert(pmsSkuAttrValue);
			}
	}
	
	
	List<PmsSkuSaleAttrValue> PmsSkuSaleAttrValueList=pmsSkuInfo.getSkuSaleAttrValueList();
	if (PmsSkuSaleAttrValueList!=null&&PmsSkuSaleAttrValueList.size()>0) {
		for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : PmsSkuSaleAttrValueList) {
			pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
			pmsSkuSaleAttrValueMapper.insert(pmsSkuSaleAttrValue);
		}
	}

		return sum>=count?"sucess":"error";
	}

	@Override
	public PmsSkuInfo skuDeataInfoById(String skuid) {
		if (StringUtils.isBlank(skuid)) {
			return null;
		}
		Jedis jedis=redisUtil.getJedis();
		//先查询缓存 
		String skuKey=Constants.Sku+skuid+Constants.info;
		String skuinfo=jedis.get(skuKey);
		if (!StringUtils.isBlank(skuinfo)) {
			//拿到缓存 
			System.out.println("缓存拿");
		PmsSkuInfo pmsSkuInfo=JSON.parseObject(skuinfo, PmsSkuInfo.class);
		jedis.close();
		return pmsSkuInfo;
			
		}
		else {
			 String token = UUID.randomUUID().toString();
			//防止击穿问题 缓存键刚好过期了 没访问到什么的 
			//设置redis自带的分布式锁 
			String ok=jedis.set(Constants.Sku+skuid+Constants.Lock, token,"nx","px",1000*10);
			if (StringUtils.isNotBlank(ok)&&ok.equals("OK")) {
				//保存成功了 代表缓存没东西 
				PmsSkuInfo pmsSkuInfo=new PmsSkuInfo();
				pmsSkuInfo.setId(skuid);
				pmsSkuInfo=pmsSkulnfoMapper.selectOne(pmsSkuInfo);
				//查询的数据不存在 (知道路径捣乱防止)
				if (pmsSkuInfo!=null) {
					//查询图片
					PmsSkuImage pmsSkuImage=new PmsSkuImage();
					pmsSkuImage.setSkuId(skuid);
					List<PmsSkuImage>	pmsSkuImageList=pmsSkuImageMapper.select(pmsSkuImage);
					pmsSkuInfo.setSkuImageList(pmsSkuImageList);
					jedis.set(skuKey, JSON.toJSONString(pmsSkuInfo));
					System.out.println("数据库存到缓存拿");
				
				}
				else {
					//没有查询到数据 redis的击穿问题 防止高并发情况下访问不存在的key 设置null值 给一个时间
					System.out.println("防止击穿设置null值");
					jedis.setex(skuKey, 60*3,JSON.toJSONString(""));
				}
				  String lockToken = jedis.get(Constants.Sku+skuid+Constants.Lock);
	                if(StringUtils.isNotBlank(lockToken)&&lockToken.equals(token)){
	                    //jedis.eval("lua");可与用lua脚本，在查询到key的同时删除该key，防止高并发下的意外的发生
	                    jedis.del(Constants.Sku+skuid+Constants.Lock);// 用token确认删除的是自己的sku的锁
	                }

	                
				
				jedis.close();
				
				return pmsSkuInfo;
			}else {
				//保存失败 代表redis有这个键
				try {
					System.out.println("穿过redis 缓存有数据 等待开始....");
					Thread.sleep(Constants.ReditDeTime);
					//过一段时间调用这个方法 键已经过期了重新保存进去 
					jedis.close();
				//	return skuDeataInfoById(skuid);
				} catch (Exception e) {
				e.printStackTrace();
				}
				return skuDeataInfoById(skuid);
			}
			
			//return null;
			
		}
		//没有数据库查 加到缓存返回去 
	}

	@Override
	public List<PmsSkuInfo> querySkuAttrValues(String productId) {
		// TODO Auto-generated method stub
		return pmsSkulnfoMapper.selectSkuSaleAttrValueListBySpu(productId);
	}

	@Autowired
	private GmallRedissonConfig gmallRedissonConfig;
	
	@Override
	public void testRedission() {
		//System.out.println(gmallRedissonConfig.redissonClient());
		//测试
		RedissonClient client=	gmallRedissonConfig.redissonClient();
		
	 	int v=0;
		Jedis jedis=redisUtil.getJedis();
		String key="sum";
		RLock lock=client.getLock("lock");
		lock.lock();
		String val=jedis.get(key);
		if (StringUtils.isBlank(val)) {
			jedis.set(key,(v)+"" );
		}
	
			jedis.set(key, (Integer.parseInt(val)+1)+"");		
	
	lock.unlock();
	jedis.close();
		
		System.out.println(jedis.get(key));
	}

	@Override
	public List<PmsSkuInfo> queryAllSku() {
		// TODO Auto-generated method stub
		return pmsSkulnfoMapper.selectAll();
	}



}
