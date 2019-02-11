package com.example.dubboprovider.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author 84681
 * Curator 锁的实现
 */
public class CuratorLockTest {

    public static void main(String[] args) throws Exception {

        CuratorFramework build = CuratorFrameworkFactory
                .builder()
                .connectString("192.168.86.90:2181")
                .sessionTimeoutMs(2000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        build.start();

        InterProcessMutex lock = new InterProcessMutex(build,"testLock");

        lock.acquire();
    }


}
