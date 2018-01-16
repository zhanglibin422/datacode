package com.huntkey.rx.demo.provider.service;

import com.huntkey.rx.sceo.demo.provider.DemoProviderApplication;
import com.huntkey.rx.sceo.demo.provider.model.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by zhaomj on 2017/4/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoProviderApplication.class)
public class SpringDataRedisTest {


    @Resource(name = "redisTemplate")
    private RedisTemplate<String,User> userRedisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void str(){
        String key = UUID.randomUUID().toString();
        String value = UUID.randomUUID().toString();

        stringRedisTemplate.opsForValue().set(key,value);
        stringRedisTemplate.opsForValue().set(key,value,123,null);

        String out = stringRedisTemplate.opsForValue().get(key);

        Assert.assertEquals(value,out);
    }

    @Test
    @Ignore
    public void pojo(){

        String id = UUID.randomUUID().toString();

        User user = new User();
        /*user.setId(1001);
        user.setAge(55);
        user.setPassword("password");
        user.setRemark("test redis serializer");
        user.setSex("gay");
        user.setUsername("Administrator");*/


        userRedisTemplate.opsForValue().set(id,user);

        User out = userRedisTemplate.opsForValue().get(id);

        Assert.assertEquals(user.toString(),out.toString());
    }

    @Test
    public void json(){
        String id = UUID.randomUUID().toString();

        User user = new User();
        /*user.setId(1001);
        user.setAge(44);
        user.setPassword("password");
        user.setRemark("test redis serializer");
        user.setSex("gay");
        user.setUsername("Administrator");*/

        userRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<User>(User.class));

        userRedisTemplate.opsForValue().set(id,user);

        User out = userRedisTemplate.opsForValue().get(id);

        Assert.assertEquals(user.toString(),out.toString());
    }
}
