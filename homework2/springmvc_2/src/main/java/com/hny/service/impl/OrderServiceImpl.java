package com.hny.service.impl;

import com.hny.bean.Order;
import com.hny.mapper.OrderMapper;
import com.hny.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;

    public List<Order> getOrdersByUserId(Integer userId){
        return orderMapper.getOrdersByUserId(userId);
    }

    public int addOrder(Order order){return orderMapper.addOrder(order);}

    public List<Order> getOrdersByAddress(String srcAddress) {
        return orderMapper.getOrdersByAddress(srcAddress);
    }

    @Transactional
    public int updateOrderStatus(Order order){return orderMapper.updateOrderStatus(order);}

    @Transactional
    public int updateCourierIdByOrderId(Order order) {
        return orderMapper.updateCourierIdByOrderId(order);
    }

    public int updateSiteIdByOrderId(Order order) {
        return orderMapper.updateSiteIdByOrderId(order);
    }

    public Order getOrderDetailByOrderId(Integer orderId){return orderMapper.getOrderDetailByOrderId(orderId);}

    public int delOrderByOrderId(Integer orderId){return orderMapper.delOrderByOrderId(orderId);}

    public Order getOrderByOrderId(Integer orderId){return orderMapper.getOrderByOrderId(orderId);}


}
