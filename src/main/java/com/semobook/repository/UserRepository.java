package com.semobook.repository;

import com.semobook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findAll();
    List<User> findAllByLastConnectionBefore(LocalDate date);
//


}
