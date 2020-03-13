package com.atguigu.gmall.pay.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.OmsOrder;
import com.atguigu.gmall.bean.PaymentInfo;
import com.atguigu.gmall.mq.ActiveMQUtil;
import com.atguigu.gmall.pay.config.AlipayConfig;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.PaymentInfoService;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.HttpclientUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;

import java.lang.ProcessBuilder.Redirect;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {


@Reference private OrderService orderService;


//确定订单页面 跳支付页面 我

@RequestMapping("to_payPage")
@LoginRequired public String to_payPage(HttpServletRequest request,ModelMap
map,String totalAmount,String outTradeNo) {
	//订单编号 总价格
	String nickName=(String)request.getAttribute("nickname"); 
	map.put("outTradeNo",outTradeNo);
	map.put("totalAmount", totalAmount); 
	map.put("nickName",nickName);
	return "index";
	}


@RequestMapping("/to_pay")

@LoginRequired public ModelAndView to_pay(HttpServletRequest request,String
outTradeNo) { //先查询订单详细信息存不存在 查询出价格(以防页面修改) 
	ModelAndView modelAndView=new
ModelAndView(); OmsOrder
omsOrder=orderService.queryUserOrderByid(outTradeNo); if(omsOrder==null) {
modelAndView.addObject("code", outTradeNo);
modelAndView.setViewName("error");

return modelAndView;
}

//重定向到支付页面 
modelAndView.addObject("totalAmount", omsOrder.getTotalAmount());

modelAndView.addObject("outTradeNo", outTradeNo);

modelAndView.setViewName("redirect:http://pay.gmall.com:8808/to_payPage");
return modelAndView; }

@Autowired private AlipayClient alipayClient;

@Reference private PaymentInfoService paymentInfoService;

//点击支付

@RequestMapping("alipay/submit")

@LoginRequired

@ResponseBody 
public String alipaySubmit(HttpServletRequest request,ModelMap map,String totalAmount,String outTradeNo) throws AlipayApiException { //这个地方查一下订单 查处价格 是最安全的 之前的不用


	AlipayTradePagePayRequest alipayRequest = new
	AlipayTradePagePayRequest();//创建API对应的request
	alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);//支付成功回调路径
	alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);//异步回调 调用支付宝
	Map
	msMap=new HashMap(); 
	msMap.put("out_trade_no",outTradeNo);
	msMap.put("product_code","FAST_INSTANT_TRADE_PAY");
	msMap.put("total_amount",0.01); msMap.put("subject","测试数据啊");
	alipayRequest.setBizContent(JSON.toJSONString(msMap)); //添加日志 用户未支付
	PaymentInfo paymentInfo=new PaymentInfo();
	paymentInfo.setOrderId(outTradeNo); paymentInfo.setOutTradeNo(outTradeNo);
	paymentInfo.setTotalAmount(new BigDecimal(totalAmount+""));
	paymentInfo.setCreateTime(new Date()); paymentInfo.setSubject("测试数据啊");
	paymentInfo.setPaymentStatus("未支付");
	String form=""; try { form =
	alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
	//先查询 避免一个订单重复添加 
	paymentInfoService.savePayinfo(paymentInfo);
	
	Map<String, Object> mqParemt=new HashMap<String, Object>();
	mqParemt.put("out_trade_no", outTradeNo);
	mqParemt.put("count", 7);
	//创建延时队列
	paymentInfoService.sendTimeMessag("PAYSTATUSE", mqParemt, 60*1000);
	
	} catch
	(AlipayApiException e) { 
		e.printStackTrace(); 
		}
	
	return form;

	}

//支付成功回调路径 订单 总价格

@RequestMapping("alipay/callback/return")
@LoginRequired public String alipayReturn(HttpServletRequest request,ModelMap map) throws AlipayApiException {
	//拿到交易这状态 测试状态
	//HttpclientUtil.doGet("");
	PaymentInfo paymentInfo= paymentInfoService.checkOrderStatus(request.getParameter("out_trade_no"));
	if (paymentInfo!=null&&"已支付".equals(paymentInfo.getPaymentStatus())) {
		System.out.println("回调路径已经支付了");
		return "paySucess";
	}
	
	
	String trade_no=request.getParameter("trade_no"); String
			trade_status=request.getParameter("trade_status"); String
			total_amount=request.getParameter("total_amount"); String
			body=request.getParameter("body"); String
			out_trade_no=request.getParameter("out_trade_no"); String
			sign_type=request.getParameter("sign_type");
			System.out.println("回调单号:"+trade_no+"//"+trade_status+"//"+total_amount+"//订单号:"+out_trade_no+"//"+sign_type);

//支付成功 
if (StringUtils.isNotBlank(trade_no)) { 
	Map msMap=new HashMap();
	msMap.put("trade_no", trade_no);
	msMap.put("trade_status",trade_status);
	msMap.put("total_amount",total_amount); 
	msMap.put("body",body);
	msMap.put("out_trade_no",out_trade_no);
	msMap.put("callbackContent",sign_type);
	orderService.updateOrdePayInforStatus(msMap); //之前判断库存系统 //订单修改状态redis修改(不过期的缓存) 修改数量 
	map.put("sucess", "成功"); }else { 
		
		map.put("sucess","失败"); }



	return "paySucess"; 

	}

	@Autowired
	private ActiveMQUtil activeMQUtil;

	@RequestMapping("mqTest")
	@ResponseBody
	public void mqText() throws JMSException {

		ConnectionFactory connectionFactory = activeMQUtil.getConnectionFactory();
		System.out.println(connectionFactory.createConnection());

	}

}