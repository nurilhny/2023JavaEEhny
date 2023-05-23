package com.hny.service;

import com.hny.bean.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductByName(String productName);

}
