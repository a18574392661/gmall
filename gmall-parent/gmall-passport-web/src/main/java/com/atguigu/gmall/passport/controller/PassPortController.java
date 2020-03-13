package com.atguigu.gmall.passport.controller;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.util.Constants;
import com.atguigu.gmall.util.HttpclientUtil;
import com.atguigu.gmall.util.JwtUtil;


@Controller
@CrossOrigin // 解决跨域问题
public class PassPortController {
 
	
	
	//去登陆页面
	@RequestMapping("index.html")
	public String index(@RequestParam(defaultValue=Constants.LOGINSUCESSU_STRING) String toUrl,ModelMap map) {
		map.put("toUrl", toUrl);
		return "index";
	}
	
	@Reference
	UserService userService;
	
	@RequestMapping("mtlogin")
	@ResponseBody
	public String mtlogin(String code) {
		System.out.println(code);
		return code;
	}
	
	//第三方登录 新浪微博返回授权码 业务逻辑的回调方法
	@RequestMapping("velogin")
	public String velogin(String code,HttpServletRequest request) {
		System.out.println(code);
		//拿到code去获取token
		//https://api.weibo.com/oauth2/access_token?client_id=1865865070&client_secret=000a6dd5a642e74a41d656599a24e27f&grant_type=authorization_code&redirect_uri=http://passport.gmall.com:8085/velogin&code=da643c21b0f09ce3ad22ee6df8430ed8
		Map<String, String> parToken=new HashMap<String, String>();
		parToken.put("client_id",Constants.App_Key);
		parToken.put("client_secret", Constants.App_Secret);
		parToken.put("grant_type", "authorization_code");
		parToken.put("redirect_uri", "http://passport.gmall.com:8085/velogin");
		parToken.put("code", code);
		String tokenMap=HttpclientUtil.doPost("https://api.weibo.com/oauth2/access_token", parToken);
		Map<String, String> token=JSON.parseObject(tokenMap, Map.class);
		if (token!=null) {
			//拿到token跟用户信息 
			String access_token=token.get("access_token");
			String uid=token.get("uid");
			//拿到access_token跟uid去查到用户信息 
			String userString=HttpclientUtil.doGet("https://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid);
			Map<String,String> userMap=JSON.parseObject(userString, Map.class);
			//不存在 数据库添加一条 
			UmsMember umsMember=new UmsMember();
			umsMember.setUsername(userMap.get("name"));
			umsMember.setNickname(userMap.get("screen_name"));
			umsMember.setAccess_code(code);
			umsMember.setAccess_token(access_token);
			umsMember.setSource_type(1);
			umsMember.setIcon(userMap.get("profile_image_url"));
			//先查询根据token存不存在存不存在 存在修改 
			//根据token查询
			UmsMember ums=userService.getUser(access_token);
			if (ums!=null) {
				//修改 
				/*
				 * umsMember.setId(ums.getId()); userService.editUser(umsMember);
				 */
				umsMember.setId(ums.getId());
			
			}else {
				int	count=userService.saveUser(umsMember);	
			}
			
			String userToken=this.getUserToken(umsMember, request);
			
			return "redirect:http://search.gmall.com:8083/index?token="+userToken;
		}
		
		return "redirect:http://passport.gmall.com:8085/index.html";
		
	}
	
	//返回登陆成功后用户的token的公共方法
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
	
	@RequestMapping("login")
	@ResponseBody
	public String login(UmsMember umsMemberd,HttpServletRequest request) {
		//查询对象
		UmsMember loginUser=userService.login(umsMemberd);
		String token=this.getUserToken(loginUser, request);
		
		return token;
		
	}
	
	//解析token
	@RequestMapping("verify")
	@ResponseBody
	public String verify(String token,String currentIp,HttpServletRequest request) {
	    // 通过jwt校验token真假
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
	
}
