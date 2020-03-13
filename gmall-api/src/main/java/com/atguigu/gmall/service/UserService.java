package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;

import java.util.List;

public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(String memberId);

	UmsMember getAllUserById(String memberId);

	void testRedis();

	List<UmsMember> userAll();

	void delUserById(String memberId);

	UmsMember login(UmsMember umsMemberd);
	
	//新增用户
	int saveUser(UmsMember umsMember);

	UmsMember getUser(String access_token);
	
	int editUser(UmsMember umsMember);

	UmsMemberReceiveAddress getReceiveAddressByMemberOrAddrId(String receiveAddressId);
}
