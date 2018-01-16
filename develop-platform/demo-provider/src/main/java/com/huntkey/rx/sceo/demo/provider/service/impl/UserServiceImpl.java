package com.huntkey.rx.sceo.demo.provider.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huntkey.rx.commons.utils.rest.Pagination;
//import com.huntkey.rx.sceo.demo.common.model.User;
//import com.huntkey.rx.sceo.demo.common.model.UserExample;

import com.huntkey.rx.sceo.demo.provider.dao.UserMapper;
import com.huntkey.rx.sceo.demo.provider.model.User;
import com.huntkey.rx.sceo.demo.provider.model.UserExample;
import com.huntkey.rx.sceo.demo.provider.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by zhaomj on 2017/4/5.
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;


    @Override
    public User selectByPrimaryKey(String id) {
        return userMapper.selectByPrimaryKey(id);    }

    @Override
    @Transactional(readOnly = false)
    public int insert(User user) {
//        事务测试
//        User edit = userMapper.selectByPrimaryKey(1);
//        edit.setAge(user.getAge());
//        userMapper.updateByPrimaryKey(edit);
        return userMapper.insert(user);
    }

    @Override
    public Pagination<User> selectByExample(String userName, int pageNum, int pageSize) {
        UserExample example = new UserExample();
        if (!StringUtils.isEmpty(userName)) {
            example.createCriteria().andUserNameLike(userName);
        }
        Page page = PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectByExample(example);
        Pagination<User> pageInfo = new Pagination<User>(list, page.getPageNum(), page.getPageSize(), page.getTotal());
        log.info("zipkin test: selectByPage provider");
        return pageInfo;
    }

    @Override
    @Transactional(readOnly = false)
    public int updateByPrimaryKey(User user) {
        log.info("update user by primaryKey '{}'", user.getId());
        return userMapper.updateByPrimaryKey(user);
    }

    @Override
    @Transactional(readOnly = false)
    public int deleteByPrimaryKey(String id) {
        return userMapper.deleteByPrimaryKey(id);
    }
}
