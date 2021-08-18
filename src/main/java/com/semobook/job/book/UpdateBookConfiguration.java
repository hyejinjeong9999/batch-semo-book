package com.semobook.job.book;

import com.semobook.domain.book.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UpdateBookConfiguration {

    private static final String JOB_NAME = "updateBookJob";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job updateBookJob(){
        return jobBuilderFactory.get(JOB_NAME)
                .start(jpaBookItemReaderStep())
                .build();
    }

    @Bean
    @JobScope
    public Step jpaBookItemReaderStep() {
        return stepBuilderFactory.get("jpaBookItemReaderStep")
                .<Book, Book>chunk(chunkSize)
                .reader(jpaBookItemReader())
                .processor(updateBook())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Book> jpaBookItemReader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("jpaBookItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select b form Book b where b.kdc is null")
                .build();
    }

    public ItemProcessor<Book, Book> updateBook() {
        return null;
    }
    public ItemWriter<Book> jpaItemWriter() {
        return null;
    }
}
