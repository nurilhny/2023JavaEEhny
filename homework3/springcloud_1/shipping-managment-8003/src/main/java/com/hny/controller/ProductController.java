package com.hny.controller;

import com.hny.entity.Product;
import com.hny.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {


    @Autowired
    ProductService productService;

    // 获得所有商品
    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    // 添加商品
    @PostMapping("/products")
    public boolean addProduct(Product product){

        int flag = productService.addProduct(product);
        if(flag==1){
            System.out.println("添加成功！");
            return true;
        }else {
            System.out.println("添加失败！");
            return false;
        }
    }

    // 通过id删除商品
    @DeleteMapping("/products/{productId}")
    public boolean delProductById(@PathVariable("productId") int productId){
        int flag = productService.delProductById(productId);
        if(flag == 1){
            System.out.println("删除成功！");
            return true;
        }else {
            System.out.println("删除失败！");
            return false;
        }
    }

}
