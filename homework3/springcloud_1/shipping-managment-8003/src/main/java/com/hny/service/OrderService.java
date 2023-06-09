package com.hny.service;

import com.hny.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

public interface OrderService {
    List<Order> getOrdersByUserId(Integer userId);

    Order getOrderDetailByOrderId(Integer orderId);

    int addOrder(Order order);

    List<Order> getOrdersByAddress(String srcAddress);

    int updateOrderStatus(Order order);

    int updateCourierIdByOrderId(Order order);

    int updateSiteIdByOrderId(Order order);

    int delOrderByOrderId(Integer orderId);

    Order getOrderByOrderId(Integer orderId);


}
