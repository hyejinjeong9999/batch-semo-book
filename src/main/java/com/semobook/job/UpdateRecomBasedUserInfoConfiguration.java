package com.semobook.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


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


    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

    private final JobExecutionListener frameMediaJobExecutionListener;
    private final StepExecutionListener frameMediaStepExecutionListener;
    private final ChunkListener frameMediaChunkListener;
    private final ItemReadListener frameMediaItemReadListener;
    private final ItemProcessListener frameMediaItemProcessListener;
    private final ItemWriteListener frameMediaItemWriteListener;


    @Value("${chunkSize:1000}")
    private int chunkSize;

//    public Job updateRecomBasedUserInfoJob() throws Exception {
//
//
//
//    }

//    @Bean
//    public Step jpaPagingItemReaderStep() {
//        return stepBuilderFactory.get("jpaPagingItemReaderStep")
//                .<Pay, Pay>chunk(chunkSize)
//                .reader(jpaPagingItemReader())
//                .writer(jpaPagingItemWriter())
//                .build();
//    }
//
//    @Bean
//    public JpaPagingItemReader<Pay> jpaPagingItemReader() {
//        return new JpaPagingItemReaderBuilder<Pay>()
//                .name("jpaPagingItemReader")
//                .entityManagerFactory(entityManagerFactory)
//                .pageSize(chunkSize)
//                .queryString("SELECT p FROM Pay p WHERE amount >= 2000")
//                .build();
//    }
//
//    private ItemWriter<Pay> jpaPagingItemWriter() {
//        return list -> {
//            for (Pay pay : list) {
//                log.info("Current Pay={}", pay);
//            }
//        };
//    }
}



