package com.semobook.job;

import com.semobook.domain.Book;
import com.semobook.domain.BookReview;
import com.semobook.domain.RecomByUserInfo;
import com.semobook.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.*;
import java.util.stream.Collectors;


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
                .start(jpaPagingItemReaderStep("F")) //성별별 책 정보 가져오기
                .next(jpaPagingItemReaderStep("M")) //성별별 책 정보 가져오기
                .build();

    }

    //step에서는 read, process, writer를 할 수 있음
    //그 외에도 before read 그런 것도 있음..
    @JobScope
    public Step jpaPagingItemReaderStep(String gender) {
        return stepBuilderFactory.get("jpaPagingItemReaderStep")
                .<UserInfo, RecomByUserInfo>chunk(chunkSize)
                .reader(jpaPagingItemReader())
                .processor(makeRecom(gender))
                .writer(jpaPagingItemWriter())
                .build();
    }

    public JpaPagingItemReader<UserInfo> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<UserInfo>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                //회원정보가 있는 유저들의 모든 리뷰 가져오기
                .queryString("select u from UserInfo u join fetch u.bookReviews br join fetch br.book where u.userGender is not null")
                .build();
    }

    //ItemProcessor<읽어올 타입, 쓸 타입>
    public ItemProcessor<UserInfo, RecomByUserInfo> makeRecom(String gender) {
        return item -> {
            log.info("gender is " + gender);
            log.info("================ItemProcessor====================");
            //남자 Map
            Map<String, Integer> manMap = new HashMap<>();
            RecomByUserInfo result = new RecomByUserInfo();
            log.info(item.getUserId());
            log.info(item.getUserGender());
            if (item.getUserGender().equals(gender)) {
                List<BookReview> reviewList = item.getBookReviews();
                List<Book> bookList = reviewList.stream().map(a -> a.getBook()).collect(Collectors.toList());

                //isbn별 개수 입력
                for (Book book : bookList) {
                    manMap.put(book.getIsbn(), manMap.getOrDefault(book.getIsbn(), 0) + 1);
                }

                //개수별 정렬
                List<String> manSortKey = new ArrayList<>(manMap.keySet());
                Collections.sort(manSortKey, (o1, o2) -> manMap.get(o2).compareTo(manMap.get(o1)));

                StringBuilder isbnSb = new StringBuilder();

                int totalCnt = manSortKey.size()<10?manSortKey.size():10;
                for (int i = 0; i < totalCnt; i++) {
                    isbnSb.append(manSortKey.get(i));
                    isbnSb.append("|");
                }
                //마지막 | 제거
                isbnSb.setLength(isbnSb.length() - 1);

                String isbn = isbnSb.toString();
                result = RecomByUserInfo.builder().recomNo(gender).isbn(isbn).build();

            }
//
//            List<String> manSortKey = new ArrayList<>(manMap.keySet());
//            Collections.sort(manSortKey, (o1, o2) -> manMap.get(o2).compareTo(manMap.get(o1)));
//
//            Map<String, Integer> womanMap = new HashMap<>();
//
//            if (item.getUserGender() == "W") {
//                womanMap.put("W", womanMap.getOrDefault("W", 0) + 1);
//            }
//
//            List<String> womanSortKey = new ArrayList<>(manMap.keySet());
//            Collections.sort(womanSortKey, (o1, o2) -> manMap.get(o2).compareTo(manMap.get(o1)));
//
//            //여자 Map

            if (result.getRecomNo() == null) return null;
            return result;
        };
    }

    public JpaItemWriter<RecomByUserInfo> jpaPagingItemWriter() {
        log.info("================ItemWriter====================");
        JpaItemWriter<RecomByUserInfo> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}




