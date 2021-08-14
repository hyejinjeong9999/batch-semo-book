package com.semobook.repository;

import com.semobook.domain.bestseller.RecomBestSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BestSellerRepository extends CrudRepository<RecomBestSeller, String> {
}
