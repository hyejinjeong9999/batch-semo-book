package com.semobook;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling
@EnableBatchProcessing
@SpringBootApplication
public class SemobookApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemobookApplication.class, args);
    }

}
