package com.example.dubboprovider.config;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.registry.zookeeper.ZookeeperRegistryFactory;
import com.alibaba.dubbo.remoting.transport.netty.NettyServer;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.cluster.LoadBalance;
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
                .connectString("192.168.229.10:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .sessionTimeoutMs(2000)
                .build();


        build.start();
        return build;
    }
}
