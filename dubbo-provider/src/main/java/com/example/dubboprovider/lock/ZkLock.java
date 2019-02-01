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

    /**
     * 共享锁 获取锁
     * @param lock
     */
    public synchronized boolean lock(final ZkClient zkClient ,String lock, String ip) {
        //创建持久化节点 锁
        boolean exists = zkClient.exists("/"+lock);
        //不存在 创建
        if (!exists) {
            zkClient.createPersistent("/"+lock);
        }
        zkClient.createEphemeralSequential("/"+lock+"/"+ip+"-",null);
        //获取锁下面的 子节点列表 （资源争夺者）
        List<String> children = zkClient.getChildren("/"+lock);
        //获取临时节点 序号
        List<Long> longList = new ArrayList<>();
        Map<String, Long> ipMap = new HashMap<>();
        Map<Long, String> sMap = new HashMap<>();
        children.forEach(item->{
            Long longId = Long.valueOf(item.split("-")[1]);
            // ip , 序列号ID
            ipMap.put(item.split("-")[0], longId);
            sMap.put(longId,item.split("-")[0] );
            longList.add(longId);
        });
        //获取集合中的最小值
        Long min = Collections.min(longList);
        Long sid = ipMap.get(ip);
        if (sid.equals(min)) {
            return true;
        }
        return false;
    }

    public void tryLock(ZkClient zkClient, String lock, Map<Long, String> sMap, Long sid) {
        String ips = sMap.get(sid);
        zkClient.subscribeDataChanges("/"+lock+"/"+ips+"-"+sid, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                boolean lock1 = lock(zkClient, lock, ips);
                if (lock1) {
                    needResourceLock();
                }
            }
        });
    }


    /**
     * 释放锁
     * 当前资源占有者 处理完业务
     * 释放锁
     * 临时节点 客户端断开 自动删除
     */
    public synchronized void unlock(final ZkClient zkClient) {
        zkClient.close();
    }

}
