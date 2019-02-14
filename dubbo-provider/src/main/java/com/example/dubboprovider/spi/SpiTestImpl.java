package com.example.dubboprovider.spi;

public class SpiTestImpl implements SpiTest {
    @Override
    public void hello() {
        System.out.println("spi 1");
    }
}
