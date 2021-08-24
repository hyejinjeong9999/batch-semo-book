package com.semobook.domain;

import lombok.*;

import javax.persistence.*;

@Table(name = "recom_by_user_info")
@Getter
@Entity
@NoArgsConstructor
public class RecomByUserInfo {
    @Id
    @GeneratedValue
    long id;

    String recomNo;
    String isbn;

    @Builder
    public RecomByUserInfo(String recomNo, String isbn) {
        this.recomNo = recomNo;
        this.isbn = isbn;
    }
}
