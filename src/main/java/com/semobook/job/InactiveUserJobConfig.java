package com.semobook.job;

import com.semobook.domain.User;
import com.semobook.repository.MemberRepository;
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


@Slf4j
@RequiredArgsConstructor
@Configuration
public class InactiveUserJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MemberRepository userRepository;

    @Bean
    public Job inactiveUserJob() {
        return jobBuilderFactory.get("inactiveUserJob")
                .start(inactiveUserStep())
                .build();
    }

    @Bean
    public Step inactiveUserStep() {
        log.info("========inactiveUserStep========");
        return stepBuilderFactory.get("inactiveUserStep")
                .<User, User>chunk(10)
                .reader(memberReader())
                .processor(this.unPaidMemberProcessor())
                .writer(this.unPaidMemberWriter())
                .build();

    }

    @Bean
    @StepScope
    public ListItemReader<User> memberReader() {

//        return new ListItemReader<>(unPaidMembers);
        return null;
    }

    public ItemProcessor<User, User> unPaidMemberProcessor() {
//        return Member::setStatusByunPaid;
        return new ItemProcessor<User, User>() {
            @Override
            public User process(User member) throws Exception {
                log.info("********** This is unPaidMemberProcessor");
//                return member.setStatusByUnPaid();
                return null;
            }
        };
    }

    //저장
    public ItemWriter<User> unPaidMemberWriter() {
//        log.info("********** This is unPaidMemberWriter");
//        return ((List<? extends Member> memberList) ->
//                member.saveAll(memberList));\
        return null;
    }
}
