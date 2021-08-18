package com.semobook.repository.book;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semobook.domain.book.Book;
import com.semobook.domain.book.QBook;

import javax.persistence.EntityManager;
import java.util.List;

import static com.semobook.domain.book.QBook.*;

public class BookRepositoryImpl implements BookRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public BookRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Book> findByNullKdc() {
        return queryFactory
                .selectFrom(book)
                .where(book.kdc.isNull())
                .fetch();
    }
}
