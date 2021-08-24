package com.semobook.job.book;

import com.semobook.domain.RecomByUserInfo;
import com.semobook.domain.book.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClientRequest;

import javax.persistence.EntityManagerFactory;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UpdateBookConfiguration {

    private static final String JOB_NAME = "updateBookJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static String LIBRARY_OF_KOREA_BASE_URL = "https://www.nl.go.kr/NL/contents/search.do?pageNum=1&pageSize=30&srchTarget=total&kwd=";

    private int chunkSize = 10;

    @Bean
    public Job updateBookJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(jpaBookItemReaderStep())
                .build();
    }

    @Bean
    public Step jpaBookItemReaderStep() {
        return stepBuilderFactory.get("jpaBookItemReaderStep")
                .<Book, Book>chunk(chunkSize)
                .reader(jpaBookItemReader())
                .processor(updateBook())
                .writer(jpaPagingItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<Book> jpaBookItemReader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("jpaBookItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select b from Book b where b.kdc is null")
                .build();
    }

    @Bean
    public ItemProcessor<Book, Book> updateBook() {
        return item -> {
//            item.updateKdc("");
            log.info("updateBook() :: book list = {}", item.getBookName());

            String url = LIBRARY_OF_KOREA_BASE_URL + item.getIsbn();
            Document doc = Jsoup.connect(url).get();
            String[] tempKdc = doc.select("#sub_content > div.content_wrap > div > div.integSearch_wrap > div.search_cont_wrap > div > div > div.search_right_section > div.section_cont_wrap > div:nth-child(1) > div.cont_list.list_type > div.row > span:nth-child(10)").text().replaceAll(" ", "").split(":");
            tempKdc = tempKdc[1].split("-");
            String kdc = tempKdc[0];
            log.info("updateBook() :: kdc is = {}", kdc);

            item.updateKdc(kdc);

            return item;
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<Book> jpaPagingItemWriter() {
        JpaItemWriter<Book> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

//    public ItemWriter<Book> jpaItemWriter() {
//        log.info("jpaItemWriter ::");
//        return list -> {
//            for (Book book : list) {
//                log.info("jpaItemWriter() :: book isbn is = {}", book.getIsbn());
//                log.info("jpaItemWriter() :: book kdc is = {}", book.getKdc() == null ? "null" : book.getKdc());
//            }
//        };
//    }
}
