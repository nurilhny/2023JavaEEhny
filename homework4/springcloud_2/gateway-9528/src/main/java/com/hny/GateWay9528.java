package com.hny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GateWay9528 {
    public static void main(String[] args) {
        SpringApplication.run(GateWay9528.class,args);
    }
}
