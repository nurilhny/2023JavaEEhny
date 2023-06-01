package com.hny.mapper;

import com.hny.bean.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<Order> getOrdersByUserId(Integer userId);

    List<Order> getOrdersByAddress(String srcAddress);

    Order getOrderDetailByOrderId(Integer orderId);

    int updateOrderStatus(@Param("order") Order order);

    int addOrder(@Param("order") Order order);

    int updateCourierIdByOrderId(@Param("order") Order order);

    int updateSiteIdByOrderId(@Param("order") Order order);

    int delOrderByOrderId(Integer orderId);

    Order getOrderByOrderId(Integer orderId);
}
