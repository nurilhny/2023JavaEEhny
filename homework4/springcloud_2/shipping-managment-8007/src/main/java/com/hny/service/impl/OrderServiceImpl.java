package com.hny.service.impl;

import com.hny.entity.Order;
import com.hny.mapper.OrderMapper;
import com.hny.service.OrderService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 通用服务降级
@DefaultProperties(defaultFallback = "getNoneOrders")
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;

    // 服务不可用（超时或出现异常），则降级处理
    // 这里要求当前线程3秒完成任务
    @HystrixCommand(fallbackMethod = "getPartOrdersByUserId",commandProperties =
            {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")})
    public List<Order> getOrdersByUserId(Integer userId){
        return orderMapper.getOrdersByUserId(userId);
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name="circuitBreaker.enabled",value = "true"),// 是否开启断路器
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value = "10"),// 熔断发生的最小请求次数，即超过该阈值才会判断是否熔断
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value = "10000"),// 时间窗口期(Open->halfOpen的时间),halfOpen状态会让一次请求访问服务，成功则close,失败则回到Open
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value = "60"),// 失败率达到多少后跳闸
    })
    public List<Order> getPartOrdersByUserId(Integer userId){

        System.out.println("只给一部分订单");
        return orderMapper.getPartOrdersByUserId(userId);
    }

    public List<Order> getNoneOrders(Integer userId){
        System.out.println("有朝一日我竟然会到这！");
        return null;
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
