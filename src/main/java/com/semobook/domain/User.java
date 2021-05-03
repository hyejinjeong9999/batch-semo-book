package com.semobook.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private LocalDate lastConnection;

    @Builder
    public User(Long id, String name, LocalDate lastConnection){
        this.name = name;
        this.lastConnection = lastConnection;
    }

    public User() {

    }
}
