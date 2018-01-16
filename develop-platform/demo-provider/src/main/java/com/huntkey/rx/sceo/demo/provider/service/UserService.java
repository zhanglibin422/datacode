package com.huntkey.rx.sceo.demo.provider.service;


import com.huntkey.rx.commons.utils.rest.Pagination;
//import com.huntkey.rx.sceo.demo.common.model.User;
import com.huntkey.rx.sceo.demo.provider.model.User;

public interface UserService {
	
	int insert(User user);
	Pagination<User> selectByExample(String userName, int pageNum, int pageSize);
	int updateByPrimaryKey(User user);
	int deleteByPrimaryKey(String id);
	User selectByPrimaryKey(String id);
}