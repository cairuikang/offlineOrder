package com.jhlc.offlineorder.core.async;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class TaskThreadPoolConfig {

    @Value("${spring.task.pool.corePoolSize}")
    private int corePoolSize;

    @Value("${spring.task.pool.maxPoolSize}")
    private int maxPoolSize;

    @Value("${spring.task.pool.keepAliveSeconds}")
    private int keepAliveSeconds;

    @Value("${spring.task.pool.queueCapacity}")
    private int queueCapacity;
      
     
}  