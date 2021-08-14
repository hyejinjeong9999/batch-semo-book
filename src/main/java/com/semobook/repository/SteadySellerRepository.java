package com.semobook.repository;

import com.semobook.domain.steadyseller.RecomSteadySeller;
import org.springframework.data.repository.CrudRepository;

public interface SteadySellerRepository extends CrudRepository<RecomSteadySeller, String> {
}
