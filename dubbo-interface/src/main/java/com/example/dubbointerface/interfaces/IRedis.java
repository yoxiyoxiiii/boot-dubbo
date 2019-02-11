package com.example.dubbointerface.interfaces;

public interface IRedis {

    void add(String ip) throws InterruptedException;


    void addByCuratorLock(String ip) throws Exception;
}
