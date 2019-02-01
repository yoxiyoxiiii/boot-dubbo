package com.example.dubboprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@SpringBootApplication
@ImportResource(locations={"classpath:dubbo-provider.xml"})
public class DubboProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboProviderApplication.class, args);
	}


	@Autowired
	private RedisTemplate redisTemplate;
	@GetMapping
	public String value() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i= 0 ;i<10;i++) {
            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    add();
                    return null;
                }
            });
        }
//        executorService.shutdown();

        return redisTemplate.opsForValue().get("test") +"";
    }

	@GetMapping("reset")
	public String reset() {
	    redisTemplate.opsForValue().set("test",0);
        return redisTemplate.opsForValue().get("test") +"";
    }

    private  synchronized String add() {
        Integer test = (Integer) redisTemplate.opsForValue().get("test");
        int value = test + 1 ;
        System.out.println("value = " + value);
        redisTemplate.opsForValue().set("test", value);
        Integer test2 = (Integer) redisTemplate.opsForValue().get("test");
        System.out.println("test2 = " +test2);
        return test2+"";
    }



}

