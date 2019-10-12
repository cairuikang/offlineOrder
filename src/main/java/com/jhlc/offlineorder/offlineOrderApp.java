package com.jhlc.offlineorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableCaching
//@ServletComponentScan(basePackages = "com.jhlc.offlineOrder") //对jar包扫描
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EnableWebMvc
public class offlineOrderApp {

    public static void main(String[] args) {
        SpringApplication.run(offlineOrderApp.class, args);
    }

}
