package com.hny.mapper;

import com.hny.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> getAllProducts();

    Product getProductByName(String productName);

    int addProduct(@Param("product") Product product);

    int delProductById(int productId);

}
