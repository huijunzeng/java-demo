package com.example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync // 开启线程池
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)// 排除数据库，这里没用上
public class AsyncThreadApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncThreadApplication.class, args);
    }
}
