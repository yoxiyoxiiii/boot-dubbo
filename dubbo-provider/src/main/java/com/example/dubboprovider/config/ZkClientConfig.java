package com.example.dubboprovider.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkClientConfig {

    @Bean
    public ZkClient zkClient() {
        return new ZkClient("192.168.229.10");
    }
}