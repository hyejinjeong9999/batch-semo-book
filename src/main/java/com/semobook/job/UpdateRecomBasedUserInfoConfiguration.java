package com.semobook.job;

import com.semobook.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.*;


/**
 * 유저 정보별 책을 통계내서 저장한다
 * 1. 성별만 : W/M
 * 2. 나이만 : 00 10 20 30 40 99
 * 3. 성별 + 나이 : W00 W10 W20 W30 W40 W99 M00 M10 M20 M30 M40 M99
 * <p>
 * [로직]
 * 1. 정보별로 유저 list가져옴 ex) 성별정보가 있는 유저 정보 별 review 데이터 가져오기
 * 2. review 꺼내서 <isbn , int> cnt
 * 3. cnt order by desc
 * 4. 상위 10개만 저장
 */

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration //Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용
public class UpdateRecomBasedUserInfoConfiguration {
    private static final String JOB_NAME = "updateRecomBasedUserInfoJob";

    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음
    private final EntityManagerFactory entityManagerFactory;


    //    @Value("${chunkSize:1000")
    // 배치할때 한꺼번에 처리하는 단위를 의미합니다 10 : db에서 10개꺼내서 10개 처리하고 10개 집어넣는거
    private int chunkSize = 10;


    //job에서는 여러가지 step을 넣을 수 있다
    @Bean
    public Job updateRecomBasedUserInfoJob() throws Exception {
        return jobBuilderFactory.get(JOB_NAME)
                .start(jpaPagingItemReaderStep()) //성별별 책 정보 가져오기
                .build();

    }

    //step에서는 read, process, writer를 할 수 있음
    //그 외에도 before read 그런 것도 있음..
    @Bean
    @JobScope
    public Step jpaPagingItemReaderStep() {
        return stepBuilderFactory.get("jpaPagingItemReaderStep")
                .<UserInfo, UserInfo>chunk(chunkSize)
                .reader(jpaPagingItemReader())
                .processor(makeRecom())
                .writer(jpaPagingItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<UserInfo> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<UserInfo>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                //회원정보가 있는 유저들의 모든 리뷰 가져오기
                .queryString("select u from UserInfo u join fetch u.bookReviews br join fetch br.book where u.userGender is not null")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<UserInfo, UserInfo> makeRecom() {
        return item -> {
            //남자 Map
            Map<String, Integer> manMap = new HashMap<>();
            if(item.getUserGender() == "M"){
                manMap.put("M", manMap.getOrDefault("M", 0) + 1);
            }

            List<String> manSortKey = new ArrayList<>(manMap.keySet());
            Collections.sort(manSortKey, (o1, o2) -> manMap.get(o2).compareTo(manMap.get(o1)));

            Map<String, Integer> womanMap = new HashMap<>();

            if(item.getUserGender() == "W"){
                womanMap.put("W", womanMap.getOrDefault("W", 0) + 1);
            }

            List<String> womanSortKey = new ArrayList<>(manMap.keySet());
            Collections.sort(womanSortKey, (o1, o2) -> manMap.get(o2).compareTo(manMap.get(o1)));

            //여자 Map


            return item;
        };
    }


    private ItemWriter<UserInfo> jpaPagingItemWriter() {
        return list -> {
            for (UserInfo u : list) {
                log.info("Current Pay={}", u.getBookReviews().size());
            }
        };
    }
}



