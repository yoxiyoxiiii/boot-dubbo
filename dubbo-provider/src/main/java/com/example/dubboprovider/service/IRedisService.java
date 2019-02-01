package com.example.dubboprovider.service;

import com.example.dubbointerface.interfaces.IRedis;
import com.example.dubboprovider.config.ZkClientConfig;
import com.example.dubboprovider.lock.ZkLock;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("iRedisService")
public class IRedisService extends BaseLockService implements IRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    ZkClientConfig zkClientConfig;

    @Autowired
    private ZkLock zkLock;
    @Override
    public  void add(String ip) throws InterruptedException {
            final ZkClient zkClient = zkClientConfig.zkClient();
            doLock(zkClient,"add",ip);
            Integer test = (Integer) redisTemplate.opsForValue().get("test");
            int value = test + 1 ;
            redisTemplate.opsForValue().set("test", value);
            Thread.sleep(1000L);
            Integer test2 = (Integer) redisTemplate.opsForValue().get("test");
            System.out.println("test2 = " +test2);

    }

    @Override
    protected void setNeedLockResource(Object object) {

        add(null);
    }
}
