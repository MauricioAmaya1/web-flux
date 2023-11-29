package com.example.tutorialwebflux.service;

import com.example.tutorialwebflux.dto.ProductDto;
import com.example.tutorialwebflux.entity.Product;
import com.example.tutorialwebflux.exceptions.CustomException;
import com.example.tutorialwebflux.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final static String NF_MESSAGE = "Product not found";
    private final static String NAME_MESSAGE = "Product name already use";

    private final ProductRepository productRepository;

    public Flux<Product> getAll(){
        return productRepository.findAll();
    }
    public Mono<Product> getById(int id){
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }

    public Mono<Product> save(ProductDto dto){

        Mono<Boolean> existName = productRepository.findByName(dto.getName()).hasElement();

        return existName.flatMap(exists -> exists ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE))
                : productRepository.save(Product.builder().name(dto.getName()).price(dto.getPrice()).build()));


    }

    public Mono<Product> update(int id, ProductDto dto){

        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        Mono<Boolean> productRepeatedName = productRepository.repeatedName(id, dto.getName()).hasElement();


        return productId.flatMap(existsId -> existsId ? // si existe el id entra, sino da error
                productRepeatedName.flatMap(
                        existsName -> existsName ? // pregunta si existe el nombre
                                Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE)) : // si existe viene aca
                                productRepository.save(new Product(id, dto.getName(), dto.getPrice())) // sino actualiza el producto
                ) :
                Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE))); // error si existe
    }


    public Mono<Void> delete(int id){

        Mono<Boolean> productId = productRepository.findById(id).hasElement();

        return productId.flatMap(
                exists -> exists ? productRepository.deleteById(id) :
                        Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }





}
