package com.example.tutorialwebflux.repository;

import com.example.tutorialwebflux.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends ReactiveCrudRepository<Product, Integer> {
}
