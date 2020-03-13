package com.atguigu.gmall.cart.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.PmsSkulnfoService;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.CookieUtil;

@Controller
public class CartController {

	@Reference
	private PmsSkulnfoService pmsSkuinfoService;
	
	
	@Reference
	private CartService cartService;
	
	
	
	@RequestMapping("One_JDshop")
	public String to_one() {
		
		return "One_JDshop";
	}
	
	@RequestMapping("cartList")
	public String cartList() {
		return "cartList";
	}
	
	//去购物车
	@LoginRequired(loginSuccess=false)
	@RequestMapping("/addToCart")
	public String to_cart(String skuId,String num,HttpServletRequest request,HttpServletResponse response,ModelMap map) {
		//拿到用户id
		String memberId=(String)request.getAttribute("memberId");
		//拿到skuid跟购买的库存
		//查询skuid的商品详细信息 
		PmsSkuInfo   pmsSkuinfo=pmsSkuinfoService.skuDeataInfoById(skuId);
		if (pmsSkuinfo==null) {
			return "redirect:success";
		}
		BigDecimal 	skuCount=new BigDecimal(num);//购买数量
		//抽取出来查询对象封装
		OmsCartItem cartItem=new OmsCartItem();
		cartItem.setCreateDate(new Date());
		cartItem.setDeleteStatus(0);
		cartItem.setPrice(new BigDecimal(pmsSkuinfo.getPrice()+""));
		cartItem.setMemberId("");//用户id 
		cartItem.setIsChecked("");//是否选中
		cartItem.setProductBrand(pmsSkuinfo.getSkuDefaultImg());//默认显示图片
		cartItem.setMemberNickname("");//用户名自
		cartItem.setPrice(pmsSkuinfo.getPrice());//*价格
		cartItem.setProductAttr("");//null了销售部属性 
		cartItem.setProductCategoryId(pmsSkuinfo.getCatalog3Id());//分类id
		cartItem.setProductId(pmsSkuinfo.getProductId());
		cartItem.setProductName(pmsSkuinfo.getSkuName());//直接以sku名字代替了
		cartItem.setProductSkuId(pmsSkuinfo.getId());//重点 skuid
		cartItem.setTotalPrice(new BigDecimal(pmsSkuinfo.getPrice()+"").multiply(skuCount));
		cartItem.setQuantity(skuCount);
		//以防万一 虽然重定向了
			
			if (StringUtils.isBlank(memberId)) {
				List<OmsCartItem> omsCartItemList=new ArrayList<OmsCartItem>();
				//判断登录是否 没有cokker
				String CookieSkuValue=CookieUtil.getCookieValue(request,Constants.CookieSkuListName ,true);
					//如果不存在 直接添加 
				if (StringUtils.isBlank(CookieSkuValue)) {
					omsCartItemList.add(cartItem);
					//添加到cookie
					CookieUtil.setCookie(request, response, Constants.CookieSkuListName, JSON.toJSONString(omsCartItemList), 60*60*24*3, true);
				
				}
				else {
					//存在的话 
					boolean ifesits=true;
					omsCartItemList=JSON.parseArray(CookieSkuValue, OmsCartItem.class);
					for (OmsCartItem omsCartItem : omsCartItemList) {
						//拿到每个的skuid跟传的skuid比较 
						if (omsCartItem.getProductSkuId().equals(skuId)) {
							//cokkie集合改变
							omsCartItem.setQuantity(omsCartItem.getQuantity().add(skuCount));
							omsCartItem.setTotalPrice(new BigDecimal(cartItem.getPrice()+"").multiply(new BigDecimal(omsCartItem.getQuantity()+"")));
							//跟新首次赋值的数量 总价格(不需要 只需要显示他这次加的数量)
						//	cartItem.setQuantity(omsCartItem.getQuantity());
							//cartItem.setTotalPrice(new BigDecimal(cartItem.getPrice()+"").multiply(new BigDecimal(cartItem.getQuantity()+"")));
							ifesits=false;
						}
					}
					//不存在的话 
					if (ifesits) {
						omsCartItemList.add(cartItem);
						
					}
					
					CookieUtil.setCookie(request, response,Constants.CookieSkuListName,JSON.toJSONString(omsCartItemList), 60*60*72, true);
				}
				
			List<OmsCartItem> items=JSON.parseArray(CookieUtil.getCookieValue(request, Constants.CookieSkuListName, true),OmsCartItem.class);
				System.out.println(items);
			}
			else {
				cartItem.setMemberId(memberId);
				//只能重新获取 不然每次没进cookie就是null的集合 
				List<OmsCartItem> cookDbSku=new ArrayList<OmsCartItem>();
				String CookieSkuValueCache=CookieUtil.getCookieValue(request,Constants.CookieSkuListName,true);
				if (StringUtils.isNotBlank(CookieSkuValueCache)) {
					cookDbSku=JSON.parseArray(CookieSkuValueCache,OmsCartItem.class);
				}
				//已经登录了
				
				//传个对象过去就 cookie集合过去 
				OmsCartItem cartItemDb=cartService.addUserCart(cartItem, cookDbSku);
			
				
			}
			
			map.put("skuInfo",cartItem);
			map.put("sum", cartItem.getQuantity());
			//把查询出来的详细信息 保存到域里面 
			/*
			 * String
			 * CookieSkuValue1=CookieUtil.getCookieValue(request,Constants.CookieSkuListName
			 * ,true); List<OmsCartItem>
			 * list=JSON.parseArray(CookieSkuValue1,OmsCartItem.class);
			 */
		return "success";
		}
		
	
	//我的购物车 
	//必须登录才行  因为购买需要登陆
	@RequestMapping("JD_Shop/One_JDshop.html")
	@LoginRequired(loginSuccess=false)
	public String UserShopCart(ModelMap map,HttpServletRequest request) {
		String memberId=(String)request.getAttribute("memberId");
		System.out.println(memberId);
		List<OmsCartItem> omsCartItemList=new ArrayList<OmsCartItem>();
		if (StringUtils.isNotBlank(memberId)) {
			 omsCartItemList=cartService.UserShopCart(memberId); 
		}else {
			String CookieSkuValue=CookieUtil.getCookieValue(request,Constants.CookieSkuListName ,true);
			if (StringUtils.isNotBlank(CookieSkuValue)) {
				omsCartItemList=JSON.parseArray(CookieSkuValue, OmsCartItem.class);
			}
		}
	
		map.put("cartList", omsCartItemList);
		return "cartList";
		
	}
	
		@RequestMapping("success")
		public String success(ModelMap map) {
			
			
			
			return "success";
		}
		
		//测试拦截器 去订单 必须登陆
		@LoginRequired
		@RequestMapping("toTrade")
		public String toTrade(String memberId) {
			System.out.println(memberId+"登录用户的id根据jwt解析的");
			return "One_JDshop";
		}
}
