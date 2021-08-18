package com.semobook.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 해당 프로젝트에서는 어느 곳에서나 JPAQueryFactory를 주입받아 Querydsl을 사용할 수 있다;
 *
 * @author hyunho
 * @since 2021/06/28
**/
@Configuration
public class QuerydslConfiguration {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }
}
