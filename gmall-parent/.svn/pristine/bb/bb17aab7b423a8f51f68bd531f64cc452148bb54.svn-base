package com.atguigu.gmall.order.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.lang.ProcessBuilder.Redirect;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.OmsCartItem;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.OmsOrderItem;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.UserService;

@Controller
public class OrderController {

	@Reference
	private UserService userService;
	
	@Reference
	private CartService cartService;
	
	@Reference
	private OrderService orderService;
	
	@RequestMapping("trade")
	@LoginRequired//必须登录
	public String index(ModelMap map,HttpServletRequest request) {
		String memberId=(String)request.getAttribute("memberId");
		List<UmsMemberReceiveAddress> umsMemberReceiveAddressList=userService.getReceiveAddressByMemberId(memberId);
		map.put("userAddressList", umsMemberReceiveAddressList);
		/*
		 * 直接拿登录后的用户的 缓存购物车数据结算 不做选中判断了 
		 * 选中判断 获取id数组 循环遍历
		 */
		List<OmsCartItem>	omsCartItemUser=cartService.UserShopCart(memberId);
		Map<String, Object> mapRety=this.getOmsOrderItemList(omsCartItemUser);
		List<OmsOrderItem> omsOrderItemList=(List<OmsOrderItem> )mapRety.get("omsOrderItemList");
		BigDecimal 	bigDecimal=(BigDecimal)mapRety.get("totalPrice");
		
		map.put("omsOrderItems", omsOrderItemList);
		map.put("totalAmount", bigDecimal);
		map.put("tradeCode", orderService.createOrderMath(memberId));	
		//每次访问订单都生成唯一码保证不重复提交
		
		return "trade";
	}
	
	//封装订单明细信息
	public Map<String, Object> getOmsOrderItemList(List<OmsCartItem> omsCartItemUser){
		
		List<OmsOrderItem> 	omsOrderItemList=new ArrayList<OmsOrderItem>();
		BigDecimal bigDecimal=new BigDecimal("0");
		for (OmsCartItem omsCartItem : omsCartItemUser) {
			//封装成订单对象 直接结算所有的算了 不做选中的了
			OmsOrderItem orderItem=new OmsOrderItem();
			orderItem.setProductCategoryId(omsCartItem.getProductCategoryId());
			orderItem.setProductId(omsCartItem.getProductSkuId());
			orderItem.setProductName(omsCartItem.getProductName());
			orderItem.setProductPrice(omsCartItem.getPrice());
			orderItem.setProductQuantity(omsCartItem.getQuantity());
			//orderItem.setCouponAmount(omsCartItem.getTotalPrice());
			orderItem.setProductPic(omsCartItem.getProductPic());
			System.out.println(orderItem.getProductPrice()+"//"+orderItem.getProductQuantity());
			bigDecimal=bigDecimal.add(new BigDecimal(orderItem.getProductPrice()+"").multiply(new BigDecimal(orderItem.getProductQuantity()+"")));
			omsOrderItemList.add(orderItem);
			System.out.println(bigDecimal);
		}
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("totalPrice", bigDecimal);
		map.put("omsOrderItemList", omsOrderItemList);
		return map;
	}
	
	//提交订单
	@RequestMapping("submitOrder")
	@LoginRequired
	public String toOrder(HttpServletRequest request,ModelMap map,String receiveAddressId,String tradeCode) {
		String memberId=(String)request.getAttribute("memberId");
		String nickname=(String)request.getAttribute("nickname");
		
		List<OmsCartItem>	omsCartItemUser=cartService.UserShopCart(memberId);
		Map<String, Object> mapRety=this.getOmsOrderItemList(omsCartItemUser);
		//封装好的订单明细
		List<OmsOrderItem> omsOrderItemList=(List<OmsOrderItem> )mapRety.get("omsOrderItemList");
		BigDecimal 	bigDecimal=(BigDecimal)mapRety.get("totalPrice");
		//拿到校验码 存在则可以操作 不存在代表重复提交了
		if (orderService.checkedOrderCode(memberId, tradeCode)) {
			String orderCode=memberId+System.currentTimeMillis();//订单编号
			//查询出地址信息
			UmsMemberReceiveAddress address=userService.getReceiveAddressByMemberOrAddrId(receiveAddressId);
			OmsOrder omsOrder=new OmsOrder();
			omsOrder.setId(orderCode);
			omsOrder.setMemberId(memberId);
			//omsOrder.setCouponId("");
			omsOrder.setCreateTime(new Date());
			omsOrder.setMemberUsername(nickname);
			omsOrder.setPayType(0);//未支付
			omsOrder.setStatus(0);//未付款
			omsOrder.setReceiverPhone(address.getPhoneNumber());
			omsOrder.setReceiverDetailAddress(address.getDetailAddress());
		//	omsOrder.setBillReceiverEmail(address.getName());
			omsOrder.setDeleteStatus(0);
			omsOrder.setReceiverName(address.getName());
			omsOrder.setConfirmStatus(0);
			omsOrder.setReceiverCity(address.getCity());
			omsOrder.setTotalAmount(bigDecimal);//总价格
			omsOrder.setPayAmount(bigDecimal);//支付总价格 实际上是减去优惠后的价格
			
			omsOrder.setOmsOrderItems(omsOrderItemList);
			//添加订单 订单明细  购物车减少（这里直接偷懒删除购物车了）
			int sucess=orderService.saveOrderItem(omsOrder);
			if (sucess!=1) {
				//订单添加失败
				map.put("errMsg", "订单添加失败");
				return "tradeFail";
			}
			
			//支付后减少库存
			cartService.deleteOmsCart(memberId);
			
			return "redirect:http://pay.gmall.com:8808/to_payPage?outTradeNo="+omsOrder.getId()+"&totalAmount="+omsOrder.getTotalAmount(); //把订单号跟总价格去支付页面
			
		}else {
			//返回重复提交页面 
			map.put("errMsg", memberId+"请不要重复提交订单");
			return "tradeFail";
		}
		
	}
	
	@RequestMapping("queryOrderUser")
	//@LoginRequired
	public String queryOrderUser(ModelMap map,HttpServletRequest request) {
		String memberId=(String)request.getAttribute("memberId");
		List<OmsOrder> omsOrder=orderService.queryUserOrder(17+"");
		map.put("orderList", omsOrder);
		return "list";
		
	}
	
	@RequestMapping("queryUserOrderByid")
	@LoginRequired
	public String queryUserOrderByid(ModelMap map,HttpServletRequest request,String orderCode) {
		
		OmsOrder omsOrder=orderService.queryUserOrderByid(orderCode);
		map.put("order", omsOrder);
		//详细页面 没有这个 占时这样 
		return "list";
		
	}
}
