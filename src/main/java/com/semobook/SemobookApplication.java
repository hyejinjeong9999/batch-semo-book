package com.semobook;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling //chron (스케쥴링) 설정
@EnableBatchProcessing //batch 설정
@SpringBootApplication
public class SemobookApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemobookApplication.class, args);
    }

}
