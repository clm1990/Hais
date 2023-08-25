package com.hais.hais1000;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Hais1000Application {


    public static void main(String[] args) {
        SpringApplication.run(Hais1000Application.class, args);
    }
}
