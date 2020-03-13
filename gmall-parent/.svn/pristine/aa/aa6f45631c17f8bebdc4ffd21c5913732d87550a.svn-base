package com.atguigu.gmall.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.CookieUtil;
import com.atguigu.gmall.util.HttpclientUtil;



@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//拿到请求路径的注解 
		//如果是null的不需要拦截(没定义)
		 HandlerMethod hm = (HandlerMethod) handler;
		 LoginRequired methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);
		//定义了 有不需要登陆 或者一定要登陆
	
		if (methodAnnotation==null) {
			System.out.println("不需要拦截");
			return true;
		}
		
		//反正登录成功两个恶值都会保存到token跟cookie
	String userCookieToken=CookieUtil.getCookieValue(request, Constants.UserCookieToken, true);
	String token=request.getParameter("token");//用户登录验证的token
	String loginSucessJwt="";
		//拿到请求的路径 如果没登录 没拿到token 那就跳到登录 穿过去这个请求的路径
		//如果没登陆 如果状态是false 购物车 默认是true 放心
	
		//判断是否登录覆盖变量 反正生成的说返回的token
	if (StringUtils.isNotBlank(userCookieToken)) {
		loginSucessJwt=userCookieToken;
	}
	if (StringUtils.isNotBlank(token)) {
		loginSucessJwt=token;
	}
		
	
	if (StringUtils.isBlank(loginSucessJwt)) {
		//没登录
		if (methodAnnotation.loginSuccess()==false) {
			//不需要登录
			
			System.out.println("不需要登录也能访问");
			
			
			return true;
			}
		//必须登录的话 那就驳回去登陆的页面
		String toUrl=request.getRequestURI();
		//  response.sendRedirect(Constants.ToLoginUrl+toUrl);
		 response.sendRedirect("http://localhost:8991/to_login");
		return false;
	}
	else {
		//不为null 放行 把token传过去解析 (其实这里写就行了)
		  String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
          if(StringUtils.isBlank(ip)){
              ip = request.getRemoteAddr();// 从request中获取ip
              if(StringUtils.isBlank(ip)){
                  ip = "127.0.0.1";
              }
          }
          //String successJson  = HttpclientUtil.doGet("http://passport.gmall.com:8085/verify?token=" + loginSucessJwt+"&currentIp="+ip);
          String successJson  = HttpclientUtil.doGet("http://localhost:8991/verify?token=" + loginSucessJwt+"&currentIp="+ip);
          Map<String, Object> map=JSON.parseObject(successJson, Map.class);
        System.out.println(successJson+"//");
          //验证成功
          if (map.get("status").equals("success")) {
        	  CookieUtil.setCookie(request, response,Constants.UserCookieToken, loginSucessJwt, 60*60*24, true);
      		//登陆了放行
        	  request.setAttribute("memberId", map.get("memberId"));
        	  request.setAttribute("nickname", map.get("nickname"));
        	  
        	  System.out.println(map.get("memberId"));
      		//题外话 这里如果直接登录了 那也应该保存 应该在登录成功以后就保存到cokkie
      		return true;
          }
          else {
        	  System.out.println("验证失败 ");
        	  String toUrl=request.getRequestURI();
    		  response.sendRedirect(Constants.ToLoginUrl+toUrl);
			return false;
		}
        
		//放行
	}
	
		
		//登录放行
		
		
		
		
	}
	
}
