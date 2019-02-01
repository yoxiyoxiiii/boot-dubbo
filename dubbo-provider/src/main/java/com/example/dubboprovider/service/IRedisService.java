package com.example.dubboprovider.service;

import com.example.dubbointerface.interfaces.IRedis;
import com.example.dubboprovider.config.ZkClientConfig;
import com.example.dubboprovider.lock.ZkLock;
import com.example.dubboprovider.lock.ZkLockDto;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("iRedisService")
public class IRedisService  implements IRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    ZkClientConfig zkClientConfig;

    @Override
    public  void add(String ip) throws InterruptedException {


        ZkClient zkClient = zkClientConfig.zkClient();

        boolean exists = zkClient.exists("/lock");
        if (!exists) {
            zkClient.createPersistent("/lock");
        }
        zkClient.createEphemeralSequential("/lock/"+ip+"-",ip);

        List<String> children = zkClient.getChildren("/lock");
        Map<Long,ZkLockDto> lockDtoMap = new HashMap<>();
        List<Long> longList = new ArrayList<>();
        children.forEach(item->{
            String[] paths = item.split("-");
            ZkLockDto build = ZkLockDto.builder()
                    .sid(paths[1])
                    .id(Long.valueOf(paths[1]))
                    .path(item)
                    .prePath(paths[0])
                    .build();
            lockDtoMap.put(Long.valueOf(paths[1]),build);
            longList.add(Long.valueOf(paths[1]));
        });
        Long min = Collections.min(longList);
        ZkLockDto zkLockDto = lockDtoMap.get(min);

        if (ip.equals(zkLockDto.getPrePath())) {
            Integer test = (Integer) redisTemplate.opsForValue().get("test");
            int value = (test==null?0:test) + 1 ;
            redisTemplate.opsForValue().set("test", value);
            Thread.sleep(1000L);
            Integer test2 = (Integer) redisTemplate.opsForValue().get("test");
            System.out.println("test2 = " +test2);
            zkClient.delete("/lock/"+ zkLockDto.getPath());
        } else {

            zkClient.subscribeDataChanges(zkLockDto.getPath(), new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {

                }

                @Override
                public void handleDataDeleted(String dataPath) throws Exception {

                    List<String> children = zkClient.getChildren("/lock");
                    Map<Long,ZkLockDto> lockDtoMap = new HashMap<>();
                    List<Long> longList = new ArrayList<>();
                    children.forEach(item->{
                        String[] paths = item.split("-");
                        ZkLockDto build = ZkLockDto.builder()
                                .sid(paths[1])
                                .id(Long.valueOf(paths[1]))
                                .path(item)
                                .prePath(paths[0])
                                .build();
                        lockDtoMap.put(Long.valueOf(paths[1]),build);
                        longList.add(Long.valueOf(paths[1]));
                    });
                    Long min = Collections.min(longList);
                    ZkLockDto zkLockDto = lockDtoMap.get(min);

                    if (ip.equals(zkLockDto.getPrePath())) {
                        Integer test = (Integer) redisTemplate.opsForValue().get("test");
                        int value = test + 1 ;
                        redisTemplate.opsForValue().set("test", value);
                        Thread.sleep(1000L);
                        Integer test2 = (Integer) redisTemplate.opsForValue().get("test");
                        System.out.println("test2 = " +test2);
                    }

                }



            });
        }

    }


}
