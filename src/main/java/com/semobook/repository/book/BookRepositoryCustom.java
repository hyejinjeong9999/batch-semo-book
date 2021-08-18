package com.semobook.repository.book;

import com.semobook.domain.book.Book;

import java.util.List;

public interface BookRepositoryCustom {

    List<Book> findByNullKdc();
}
