package com.semobook.domain.bestseller;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


/**
 * 베스트셀러 추천 (배치)
 * RECOM_BEST_SELLER_1 : bestSellsr 1위
 *
 * @author hyejinzz
 * @since 2021-05-29
 **/
@Getter
@RedisHash("RECOM_BEST_SELLER")
public class RecomBestSeller {
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
    public RecomBestSeller(String rank,
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
}
