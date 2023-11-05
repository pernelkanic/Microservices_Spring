package com.springmicroservices.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springmicroservices.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
}
