package com.example.dubboprovider.service;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.*;

public abstract class BaseLockService {

    List<Long> longList = new ArrayList<>();
    Map<String, Long> ipMap = new HashMap<>();
    Map<Long, String> sMap = new HashMap<>();
    protected abstract void setNeedLockResource();

    private boolean getLock(ZkClient zkClient ,String lock, String ip) {
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

    private void tryLock(ZkClient zkClient, String lock, Map<Long, String> sMap, Long sid) {
        String ips = sMap.get(sid);
        zkClient.subscribeDataChanges("/" + lock + "/" + ips + "-" + sid, new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                boolean lock1 = getLock(zkClient, lock, ips);
                if (lock1) {
                    setNeedLockResource();
                }else {
                    Long sid = ipMap.get(ips);
                    tryLock(zkClient, lock, sMap,sid);
                }
            }
        });
    }

    public void doLock(ZkClient zkClient ,String lock, String ip) {
        boolean lock1 = getLock(zkClient, lock, ip);
        if (lock1) {
            setNeedLockResource();
        } else {
            Long sid = ipMap.get(ip);
            tryLock(zkClient,lock,sMap, sid);
        }

    }

}
