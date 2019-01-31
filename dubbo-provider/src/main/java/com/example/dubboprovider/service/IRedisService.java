package com.example.dubboprovider.service;

import com.example.dubbointerface.interfaces.IRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("iRedisService")
public class IRedisService implements IRedis {

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void add() {
        Integer test = (Integer) redisTemplate.opsForValue().get("test");
        int value = test + 1 ;
        System.out.println("value = " + value);
        redisTemplate.opsForValue().set("test", value);
        Integer test2 = (Integer) redisTemplate.opsForValue().get("test");
        System.out.println("test2 = " +test2);
    }
}
