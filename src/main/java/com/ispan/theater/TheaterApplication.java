package com.ispan.theater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableAspectJAutoProxy
@ServletComponentScan
//@EnableScheduling
public class TheaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheaterApplication.class, args);
    }

}
