package com.semobook.job;

import com.semobook.domain.User;
import com.semobook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class InactiveUserJobConfig {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory inactiveUserStep; // 생성자 DI 받음

    private final UserRepository userRepository;


    @Bean
    public Job inactiveUserJob(
            JobBuilderFactory jobBuilderFactory,
            Step inactiveJobStep
    ) {
        log.info("********** This is inactiveUserJob");
        return jobBuilderFactory.get("inactiveUserJob")
                .preventRestart()
                .start(inactiveJobStep)
                .build();
    }


    @Bean
    public Step inactiveJobStep(
            StepBuilderFactory stepBuilderFactory
    ) {
        log.info("********** This is inactiveUserStep");
        return stepBuilderFactory.get("inactiveUserStep")
                .<User, User> chunk(3)
                .reader(inactiveUserReader())
                .processor(inactiveUserProcessor())
                .writer(inactiveUserWriter())
                .build();
    }


    @Bean
    @StepScope
    public ListItemReader<User> inactiveUserReader() {
        List<User> oldUsers = userRepository.findAllByLastConnectionBefore(LocalDate.now().minusMonths(3));
        return new ListItemReader<>(oldUsers);
    }

    public ItemProcessor<User, User> inactiveUserProcessor() {
        log.info("========memberProcessor========");
        return User::setIncative;
    }

    //저장
    public ItemWriter<User> inactiveUserWriter() {
        log.info("========memberWriter========");

        return ((List<? extends User> users) ->
                userRepository.saveAll(users));

    }
}
