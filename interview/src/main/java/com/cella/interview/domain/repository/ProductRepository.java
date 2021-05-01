package com.cella.interview.domain.repository;

import com.cella.interview.domain.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    Optional<Product> getByName(String name);

}
