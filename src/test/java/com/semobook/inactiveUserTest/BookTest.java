package com.semobook.inactiveUserTest;
import com.semobook.domain.book.Book;
import com.semobook.repository.book.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@Transactional
@Slf4j
public class BookTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    @DisplayName("FIND_LIST_KDC_IS_NULL")
    public void FIND_LIST_KDC_IS_NULL(){
        //give

        //when
        List<Book> bookList = bookRepository.findByNullKdc();
//        log.info("book list count is = {}",bookList.size());
        //then

    }
}
