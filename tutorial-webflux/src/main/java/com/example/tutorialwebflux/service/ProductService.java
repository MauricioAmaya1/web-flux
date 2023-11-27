package com.example.tutorialwebflux.service;

import com.example.tutorialwebflux.entity.Product;
import com.example.tutorialwebflux.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Flux<Product> getAll(){
        return productRepository.findAll();
    }
    public Mono<Product> getById(int id){
        return productRepository.findById(id);
    }

    public Mono<Product> save(Product product){
        return productRepository.save(product);
    }

    public Mono<Product> update(int id, Product product){
        return productRepository.save(new Product(id, product.getName(), product.getPrice()));
    }


    public Mono<Void> delete(int id){
        return productRepository.deleteById(id);
    }





}
