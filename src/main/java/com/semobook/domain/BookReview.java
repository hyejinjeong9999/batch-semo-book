package com.semobook.domain;

import com.semobook.domain.book.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class BookReview {
    @Id
    @GeneratedValue
    private long reviewNo;

    private int rating;
    private String reviewContents;
    private LocalDateTime createDate;
    private int declaration;

    //NORMAl, BLIND
    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn" )
    private Book book;


    @Builder
    public BookReview(int rating, String reviewContents, LocalDateTime createDate, int declaration, Book book, UserInfo userInfo) {
        this.rating = rating;
        this.reviewContents = reviewContents;
        this.createDate = createDate;
        this.declaration = declaration;
        this.book = book;
        this.userInfo = userInfo;
    }

    public void changeBookReview(int rating, String reviewContents) {
        this.rating = rating;
        this.reviewContents = reviewContents;
    }
}
