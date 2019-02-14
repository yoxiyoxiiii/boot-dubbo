package com.example.dubboprovider.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * java spi 技术
 * service provider interface
 * 面向接口编程思想的体现
 * 一个接口多个实现
 */
public class SpiMain {

    public static void main(String[] args) {
        //ServiceLoader jdk spi 加载器
        // 加载 "META-INF/services/" + 接口全路径文件（com.example.dubboprovider.spi.SpiTest）
        // 文件内容 是接口的实现
        ServiceLoader<SpiTest> serviceLoader = ServiceLoader.load(SpiTest.class);

        //得到接口实现类集合
        Iterator<SpiTest> iterator = serviceLoader.iterator();

        while (iterator.hasNext()) {
            SpiTest next = iterator.next();
            next.hello();
        }
    }
}
