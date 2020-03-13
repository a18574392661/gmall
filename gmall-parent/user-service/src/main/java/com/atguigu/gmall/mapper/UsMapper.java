package com.atguigu.gmall.mapper;

import org.apache.ibatis.annotations.Update;

import com.atguigu.gmall.bean.UmsMember;

import tk.mybatis.mapper.common.Mapper;

public interface UsMapper extends Mapper<UmsMember> {

	@Update("update username=#{username},password=#{password},phone=#{phone},nickname=#{nickname} set where id=#{id}")
	void updateBy(UmsMember umsMember);

}
