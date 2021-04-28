package com.wes;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.wes.adopt")
@EnableCaching
public class AdoptApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdoptApplication.class, args);
    }

}
