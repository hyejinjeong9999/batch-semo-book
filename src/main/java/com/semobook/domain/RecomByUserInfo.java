package com.semobook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class RecomByUserInfo {
    @Id
    String recomNo;
    String isbn;
}
