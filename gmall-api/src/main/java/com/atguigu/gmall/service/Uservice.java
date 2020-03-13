package com.atguigu.gmall.service;

import java.util.List;

import com.atguigu.gmall.bean.UmsMember;

public interface Uservice {
	
	public List<UmsMember> all();
	public void add(UmsMember umsMember);
	public void del(String id);
	public void edit(UmsMember umsMember);
	public UmsMember queryById(String id);
	
	public UmsMember login(UmsMember umsMember);
}
