package com.atguigu.gmall.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.service.Uservice;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.JwtUtil;

import junit.framework.Test;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.ProcessBuilder.Redirect;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserTestController {

@Reference
Uservice uservice;
	
@RequestMapping("to_login")
public String to_login() {
	
	
	
	return "login";
}
    

@ResponseBody
@RequestMapping("login")
public String login(UmsMember umsMember,HttpServletRequest request) {
	String token="";
UmsMember user=uservice.login(umsMember);
	if (user!=null) {
		//转换token
	token=getUserToken(umsMember, request);
		
	}else {
		token="error";
	}
	
	return token;
	
}

@RequestMapping("verify")
@ResponseBody
public String verify(String token,String currentIp,HttpServletRequest request) {
    // 通过jwt校验token真假
	System.out.println(token);
    Map<String,String> map = new HashMap<>();
    Map<String, Object> decode = JwtUtil.decode(token, Constants.JwyKey, currentIp);
	if (decode!=null) {
		   map.put("status","success");
            map.put("memberId",(String)decode.get("memberId"));
            map.put("nickname",(String)decode.get("nickname"));
	}else {
		 map.put("status","error");
	}
	
	return JSON.toJSONString(map);
	
}


public String getUserToken(UmsMember umsMember,HttpServletRequest request) {
	String token="";
	
	//如果登录成功 jwt根据用户id生成一个token  返回去 然后拦截 拿到token 保存到cookei里面
	Map<String, Object> map=new HashMap<String, Object>();
	map.put("memberId",umsMember.getId());
	map.put("nickname",umsMember.getNickname());
	//拿到ip地址
    String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
    if(StringUtils.isBlank(ip)){
        ip = request.getRemoteAddr();// 从request中获取ip
        if(StringUtils.isBlank(ip)){
            ip = "127.0.0.1";
        }

	//按照jwt加密
	

}

    token= JwtUtil.encode(Constants.JwyKey, map, ip);
    return token;
}


//查询
	@RequestMapping("all")
	@LoginRequired
	public String all(HttpServletRequest request,ModelMap map) {
		System.out.println(request.getAttribute("memberId"));
			map.put("list",uservice.all());	
		
		return "list";
	}
	
	
	
	@RequestMapping("add")@LoginRequired
	public String add(HttpServletRequest request,ModelMap map,UmsMember umsMember) {
		uservice.add(umsMember);
		
		return "redirect:all";
	}
	
	
	@RequestMapping("to_add")@LoginRequired
	public String to_add(HttpServletRequest request,ModelMap map) {
		
		
		return "add";
	}
	
	
	@RequestMapping("to_edit")
	public String to_edit(HttpServletRequest request,ModelMap map,String id) {
	map.put("u", uservice.queryById(id));
		
		return "edit";
	}
	
	
	@RequestMapping("edit")
	public String edit(HttpServletRequest request,ModelMap map,UmsMember umsMember) {
		uservice.edit(umsMember);
		
		return "redirect:all";
	}
	
	
	@RequestMapping("del")
	public String del(String id) {
		uservice.del(id);
		return "redirect:all";
	}
	
	
	

}
