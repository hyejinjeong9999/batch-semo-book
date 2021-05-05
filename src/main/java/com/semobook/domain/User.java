package com.semobook.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
@Data
public class User {
    @Id
    @GeneratedValue
    private Long no;

    @Column
    private String userName;

    @Column
    private String userStatus;

    @Column
    private LocalDate lastConnection;

    @Builder
    public User(String userName,String userStatus, LocalDate lastConnection){
        this.userName = userName;
        this.userStatus = userStatus;
        this.lastConnection = lastConnection;
    }

    public User setIncative(){
        this.setUserStatus("INACTIVE");
        return this;
    }

    public User() {

    }
}
