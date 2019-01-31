package com.example.dubboprovider.lock;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * zk
 * 实现分布式锁
 * @author yj
 */
@Component
public class ZkLock {

    @Autowired
    private ZkClient zkClient;

    /**
     * 共享
     * @param lock
     */
    public void lock(String lock) {

        zkClient.createPersistent(lock);

    }
}
