package com.semobook.repository.book;

import com.semobook.domain.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String>{
//    List<Book> findByNullKdc();
}
