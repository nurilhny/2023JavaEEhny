package com.hny.mapper;

import com.hny.bean.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> getAllProducts();

    Product getProductByName(String productName);

}
