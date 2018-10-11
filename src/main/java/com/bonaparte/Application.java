package com.bonaparte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;

/**
 * Created by yangmingquan on 2018/10/11.
 */
@SpringBootApplication
@Async
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
