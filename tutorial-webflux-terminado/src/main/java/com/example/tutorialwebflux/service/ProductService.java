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

    private final static String NF_MESSAGE = "product not found";
    private final static String NAME_MESSAGE = "product name already in use";

    private final ProductRepository productRepository;

    public Flux<Product> getAll(){
        return productRepository.findAll();
    }
    public Mono<Product> getById(int id){
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE)));
    }

    public Mono<Product> save(ProductDto dto) {
        Mono<Boolean> existsName = productRepository.findByName(dto.getName()).hasElement();
        return existsName.flatMap(exists -> exists ? // consulta si existe otro producto con "x" nombre
                Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE)) // si existe da error
                : productRepository.save(Product.builder().name(dto.getName()).price(dto.getPrice()).build())); // sino crea el producto
    }

    public Mono<Product> update(int id, ProductDto dto) {
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        Mono<Boolean> productRepeatedName = productRepository.repeatedName(id, dto.getName()).hasElement();

        return productId.flatMap(
                existsId -> existsId ? // consulta si existe el id
                        productRepeatedName.flatMap(existsName -> existsName ? // si existe el nombre
                                Mono.error(new CustomException(HttpStatus.BAD_REQUEST, NAME_MESSAGE)) // da error porque ya existe
                                : productRepository.save(new Product(id, dto.getName(), dto.getPrice()))) // sino actualiza el producto
                        : Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE))); // sino se encuentra el id, da este error
    }

    public Mono<Void> delete(int id) {
        Mono<Boolean> productId = productRepository.findById(id).hasElement();
        return productId.flatMap(exists -> exists ? // consulta si existe el id para eliminar
                productRepository.deleteById(id) // si existe lo borra
                : Mono.error(new CustomException(HttpStatus.NOT_FOUND, NF_MESSAGE))); // sino da error
    }


}
