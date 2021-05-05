package com.sixpoints;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TimesequenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimesequenceApplication.class, args);
    }

}
