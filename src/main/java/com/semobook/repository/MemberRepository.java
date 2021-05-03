package com.semobook.repository;

import com.semobook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Member;
import java.util.List;

public interface MemberRepository extends JpaRepository<User,Long> {
    List<User> findAll();
    List<User> findBystatusEquals();

}
