package com.hny.service;

import com.hny.bean.Order;

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
