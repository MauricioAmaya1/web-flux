package com.example.tutorialwebflux.repository;

import com.example.tutorialwebflux.entity.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository  extends ReactiveCrudRepository<Product, Integer> {

}
