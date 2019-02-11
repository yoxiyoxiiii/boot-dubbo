package com.example.dubboprovider.config;

import org.I0Itec.zkclient.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkClientConfig {


    public ZkClient zkClient() {
        return new ZkClient("192.168.86.90");
    }

    @Bean
    public CuratorFramework curatorFramework() {
        CuratorFramework build = CuratorFrameworkFactory
                .builder()
                .connectString("192.168.86.90:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .sessionTimeoutMs(2000)
                .build();
        build.start();
        return build;
    }
}
