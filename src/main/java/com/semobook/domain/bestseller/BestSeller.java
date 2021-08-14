package com.semobook.domain.bestseller;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//@Entity
//@NoArgsConstructor
//@Getter
//@Table(name = "BOOK_BEST_SELLER")
public class BestSeller {
    @Id
    @GeneratedValue
    @Column(name = "BEST_NO")
    private int no;

    @Column(name = "BEST_RANK")
    private String rank;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "CATEGORY")
    private String titleCategory;

    @Column(name = "DETAIL_CATEGORY")
    private String detailCategory;

    @Builder
    public BestSeller(String rank, String isbn, String titleCategory, String detailCategory){
        this.rank = rank;
        this.isbn = isbn;
        this.titleCategory = titleCategory;
        this.detailCategory = detailCategory;
    }
}
