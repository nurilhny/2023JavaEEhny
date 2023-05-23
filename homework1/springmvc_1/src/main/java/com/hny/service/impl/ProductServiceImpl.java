package com.hny.service.impl;

import com.hny.bean.Product;
import com.hny.mapper.ProductMapper;
import com.hny.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    public List<Product> getAllProducts(){
        return productMapper.getAllProducts();
    }

    public Product getProductByName(String productName){return productMapper.getProductByName(productName);}


}
