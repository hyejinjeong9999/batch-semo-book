//package com.semobook.job;
//
//
//import com.semobook.tasklets.InactiveUserTasklet;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//public class InactiveUserJobConfigTasklet {
//
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public Job inactiveUserJob() {
//        return jobBuilderFactory.get("inactiveUserJob")
//                .start(inactiveUserStep())
//                .build();
//    }
//
//    private Step inactiveUserStep() {
//        return stepBuilderFactory.get("inactiveUserStep")
//                .tasklet(new InactiveUserTasklet())
//                .build();
//    }
//
//
//}
