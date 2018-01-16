package com.huntkey.rx.demo.provider.service;

import com.huntkey.rx.commons.utils.rest.Pagination;
import com.huntkey.rx.sceo.demo.provider.DemoProviderApplication;
import com.huntkey.rx.sceo.demo.provider.model.User;
import com.huntkey.rx.sceo.demo.provider.service.UserService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhaomj on 2017/4/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoProviderApplication.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    @Ignore
    public void add(){
        User user = new User();
        /*user.setId(5);
        user.setAge(15);
        user.setPassword("");
        user.setRemark("test generator");
        user.setSex("M");
        user.setUsername("user5");*/
        Assert.assertEquals(1,userService.insert(user));
    }

    @Test
    public void page(){
        String userName = "";

        Pagination<User> pageInfo = userService.selectByExample(userName,1,5);
        Assert.assertEquals(5,pageInfo.getList().size());
        Assert.assertEquals(6,pageInfo.getTotal());

        userName = "test";
        pageInfo = userService.selectByExample(userName,1,5);
        Assert.assertEquals(1,pageInfo.getList().size());
        Assert.assertEquals(1,pageInfo.getTotal());
    }

}
