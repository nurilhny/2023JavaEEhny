package com.hny.service;

import com.hny.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductByName(String productName);

    int addProduct(Product product);

    int delProductById(int productId);



}
