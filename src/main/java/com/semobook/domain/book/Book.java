package com.semobook.domain.book;

import com.semobook.domain.BookReview;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Book {
    @Id
    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "BOOK_NAME")
    private String bookName;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "PUBLISHER")
    private String publisher;

    @Column(name = "contents")
    private String contents;

    @Column(name = "KDC")
    private String kdc;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "KEYWORD")
    private String keyword;

    @Column(name = "BOOK_IMAGE")
    private String img;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookReview> bookReviewList = new ArrayList<>();

    @Builder
    public Book(String isbn, String bookName, String author, String publisher,
                String kdc, String category, String keyword, String img, String contents) {
        this.isbn = isbn;
        this.bookName = bookName;
        this.author = author;
        this.publisher = publisher;
        this.kdc = kdc;
        this.category = category;
        this.keyword = keyword;
        this.img = img;
        this.contents = contents;
    }

    /**
     * update kdc
     *
     * @author hyunho
     * @since 2021/08/23
    **/
    public void updateKdc(String kdc){
        this.kdc = kdc;
    }
}
