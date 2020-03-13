package com.atguigu.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;

import junit.framework.Test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

	@Reference //用dubbo的自动注入 
    UserService userService;

	@RequestMapping("index2")
	public String index2() {
		
		
		return "a";
	}
	@RequestMapping("/testrr")
	@ResponseBody
	public void Test() {
		userService.testRedis();
		
	}
	
	@RequestMapping("/userAll")
	@ResponseBody
	public List<UmsMember> userAll(){
		
		return userService.userAll();
	}
	
	
    @RequestMapping("getReceiveAddressByMemberId")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId){
        List<UmsMemberReceiveAddress> umsMemberReceiveAddresses = userService.getReceiveAddressByMemberId(memberId);
        return umsMemberReceiveAddresses;
    }

    

    @RequestMapping("getAllUserById")
    @ResponseBody
    public UmsMember getAllUserById(String memberId){
    	UmsMember umsMember = userService.getAllUserById(memberId);
        return umsMember;
    }
    
    @ResponseBody
    @RequestMapping("/del")
    public void delUserById(String memberId){
    	userService.delUserById(memberId);
       
    }
    

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){
        List<UmsMember> umsMembers = userService.getAllUser();
        return umsMembers;
    }

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello user";
    }


    

}
