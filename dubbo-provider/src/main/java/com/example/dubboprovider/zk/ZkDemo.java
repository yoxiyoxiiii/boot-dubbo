package com.example.dubboprovider.zk;

import com.example.dubboprovider.lock.ZkLockDto;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.omg.CORBA.LongHolder;

import java.util.*;

public class ZkDemo {

    public static void main(String[] args) throws InterruptedException {

        ZkClient zkClient = new ZkClient("192.168.86.90");

//        create(zkClient);
//        update(zkClient);
//        delete(zkClient);
//        get(zkClient);

//        createNodeEphemeral(zkClient);
        ZkLockDto zkLockDto = createEphemeralSequential(zkClient);



            String s = "/lock/"+zkLockDto.getPath();
            zkClient.subscribeDataChanges(s, new IZkDataListener() {
                @Override
                public void handleDataChange(String s, Object o) throws Exception {

                }

                @Override
                public void handleDataDeleted(String s) throws Exception {
                    System.out.println(s);
                }
            });
           Thread.sleep(600*1000L);


    }

    /**
     * /**
     * 创建一个节点
     * zk 存储数据方式 ： path value --> key ,value
     * CreateMode 节点类型
     * PERSISTENT 持久化节点 ： 客户端会话结束，不会删除
     * PERSISTENT_SEQUENTIAL 持久化有序节点
     * EPHEMERAL 临时节点 会话结束，节点自动删除
     *
     * EPHEMERAL_SEQUENTIAL ；临时有序节点
     *
     * zkClient 创建节点
     */
    public static void create(ZkClient zkClient) {


        //注册监听
        //节点变化回调
        zkClient.subscribeChildChanges("/", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("parentPath = " +parentPath);
                currentChilds.forEach(item->{
                    System.out.println(item);
                });
            }
        });
        zkClient.create("/createTest4",123456, CreateMode.PERSISTENT);

    }

    /**
     * 更新节点并设置事件监听
     */
    public static void update(ZkClient zkClient) {

        //先设置事件监听，再操作节点
        zkClient.subscribeDataChanges("/createTest4", new IZkDataListener() {
            //节点数据改变，回调
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println(data);
                System.out.println(dataPath);
            }

            //结束数据删除，回调
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {

            }
        });
        zkClient.writeData("/createTest4",9999);
    }

    public static void delete(ZkClient zkClient) {
        zkClient.subscribeDataChanges("/createTest4", new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }

            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println(dataPath + "sssssssss");
            }
        });

        zkClient.subscribeChildChanges("/", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("parentPath  = " + parentPath);
                currentChilds.forEach(item->{
                    System.out.println(item);
                });
            }
        });
        //删除节点
        zkClient.delete("/createTest4");
    }

    /**
     * 获取指定节点的子节点 列表
     * @param zkClient
     */
    public static void get(ZkClient zkClient) {
        List<String> children = zkClient.getChildren("/");
        children.forEach(item->{
            System.out.println(item);
        });
    }

    /**
     * 创建临时节点
     * @param zkClient
     */
    public static void createNodeEphemeral(ZkClient zkClient) {

        zkClient.subscribeChildChanges("/", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath);
                currentChilds.forEach(item->{
                    System.out.println(item);
                });
            }
        });
        //创建临时节点
        zkClient.createEphemeral("/testEphemeral");
    }

    /**
     * 创建有序临时节点
     */
    public static ZkLockDto createEphemeralSequential(ZkClient zkClient) {
            zkClient.createPersistent("/lock");

            zkClient.createPersistentSequential("/lock/test1-","qaz1");
            zkClient.createPersistentSequential("/lock/test1-","qaz2");
            zkClient.createPersistentSequential("/lock/test1-","qaz3");
            zkClient.createPersistentSequential("/lock/test1-","qa4");

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

        return zkLockDto;
    }
}
