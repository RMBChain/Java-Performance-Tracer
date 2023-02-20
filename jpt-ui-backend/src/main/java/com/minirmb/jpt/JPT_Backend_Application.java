package com.minirmb.jpt;

import com.minirmb.jpt.orm.services.AnalysisRangeMongoService;
import com.minirmb.jpt.receiver.NIOServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

@SpringBootApplication
@EnableAsync
@Component
@Configuration
public class JPT_Backend_Application {
    @Resource
    private NIOServer server;

    @Resource
    private AnalysisRangeMongoService analysisRangeMongoService;

    @PreDestroy
    public void preDestroy() throws Exception {
    }

    @PostConstruct
    public void postConstruct() throws Exception {
        analysisRangeMongoService.initOnStartup();
        server.start();
    }

    @Bean
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(16);
        return taskScheduler;
    }

    public static void main(String[] args) {
        SpringApplication.run(JPT_Backend_Application.class, args);
    }
}



