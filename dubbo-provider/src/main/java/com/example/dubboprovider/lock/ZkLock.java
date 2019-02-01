package com.example.dubboprovider.lock;

import com.example.dubboprovider.config.ZkClientConfig;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * zk
 * 实现分布式锁
 * @author yj
 */
@Component
public class ZkLock {

    @Autowired
    private ZkClientConfig zkClientConfig;

}
