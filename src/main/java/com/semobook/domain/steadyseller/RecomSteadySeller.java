package com.semobook.domain.steadyseller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash("RECOM_STEADY_SELLER")
public class RecomSteadySeller {
    @Id
    private String rank;
    private String isbn;
    private String bookName;
    private String author;
    private String publisher;
    private String kdc;
    private String category;
    private String keyword;
    private String img;

    @Builder
    public RecomSteadySeller(String rank,
                             String isbn,
                             String bookName,
                             String author,
                             String publisher,
                             String kdc,
                             String category,
                             String keyword,
                             String img
    ) {
        this.rank = rank;
        this.isbn = isbn;
        this.bookName = bookName;
        this.publisher = publisher;
        this.author = author;
        this.kdc = kdc;
        this.category = category;
        this.keyword = keyword;
        this.img = img;
    }

//    public RecomSteadySeller createBestSeller(){
//        return new RecomSteadySeller(RecomSteadySeller.builder()
//                .rank(category + "_" +idx )
//                .isbn(isbn)
//                .bookName(title)
//                .author(author)
//                .publisher(publisher)
//                .kdc("")
//                .category(category)
//                .keyword(detailCategory)
//                .img(imgPath)
//                .build());
//    }
}