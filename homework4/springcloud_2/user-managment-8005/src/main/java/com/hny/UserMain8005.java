package com.hny;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UserMain8005 {
    public static void main(String[] args) {
        SpringApplication.run(UserMain8005.class,args);
    }
}
