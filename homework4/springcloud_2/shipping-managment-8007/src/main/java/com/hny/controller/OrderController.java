package com.hny.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hny.entity.Order;
import com.hny.entity.Product;
import com.hny.entity.User;
import com.hny.service.*;
import com.hny.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@PreAuthorize("hasAnyAuthority('user','courier')")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderFeignService orderFeignService;

    @Autowired
    ProductService productService;

    @Autowired
    SiteService siteService;


    @Resource
    IMessageProvider messageProvider;

    // 用户通过userId获取订单
    @GetMapping("/orders/user/{userId}")
    public List<Order> getOrdersByUserId(@PathVariable("userId") int userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return orders;
    }


    // 根据ID获取订单
    @GetMapping("/orders/{orderId}")
    public Order getOrderByOrderId(@PathVariable("orderId") Integer orderId){
        Order order = orderService.getOrderByOrderId(orderId);
        return order;
    }

    // 获取运货员所有订单
    @GetMapping("/orders/courier/{userId}")
    public List<Order> getOrdersByCourierId(@PathVariable("userId") int userId) throws JsonProcessingException {
        R r = orderFeignService.getUserByUserId(userId);
        String courierStr = (String)r.getData().get("user");
        System.out.println(courierStr);
        ObjectMapper mapper = new ObjectMapper();
        User courier = mapper.readValue(courierStr,User.class);

        List<Order> orders = orderService.getOrdersByAddress(courier.getAddress());

        return orders;

    }

    // 添加订单
    @PostMapping("/orders/user")
    public Boolean addOrder(@RequestBody Order order) {

        Product product = productService.getProductByName(order.getProductName());
        order.setSiteId(product.getSiteId());
        order.setSrcAddress(product.getSrcAddress());
        order.setTotalPrice(product.getPrice()*order.getQuantity());
        System.out.println("---------addOrder-----------");
        System.out.println(order);
        int flag = orderService.addOrder(order);

        if(flag==1){
            System.out.println("添加成功！");
            messageProvider.send(order.getUserId());
            return true;
        }else {
            System.out.println("添加失败！");
            return false;
        }


    }

    // 获取订单详细信息
    @GetMapping("/orders/user/detail/{orderId}")
    public Order orderDetail(@PathVariable("orderId") int orderId){

        Order order = orderService.getOrderDetailByOrderId(orderId);

        return order;
    }

    // 用户更改订单状态
    @PutMapping("/orders/user/{orderId}/{orderStatus}")
    public Boolean orderTypeUpdateByUser(@PathVariable("orderId") int orderId,
                                  @PathVariable("orderStatus") String orderStatus
                                  ){

        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatus(orderStatus);

        int flag = orderService.updateOrderStatus(order);

        if(flag==1){
            System.out.println("修改成功！");
            return true;
        }else {
            System.out.println("修改失败！");
            return false;
        }


    }

    // 运货员更改订单状态
    @PutMapping("/orders/courier")
    @Transactional
    public Boolean orderTypeUpdateByCourier(@RequestBody Order order){

        int flag1 = orderService.updateOrderStatus(order);
        int flag2 = orderService.updateCourierIdByOrderId(order);

        if(flag1==1&&flag2==1){
            System.out.println("修改成功！");
            return true;
        }else {
            System.out.println("修改失败！");
            return false;
        }


    }

    // 运输员更改站点
    @PutMapping("/orders/courier/changeSite")
    @Transactional
    public Boolean changeSite(@RequestBody Order order){

        int flag1 = orderService.updateCourierIdByOrderId(order);

        int flag2 = orderService.updateSiteIdByOrderId(order);

        if(flag1==1&&flag2==1){
            System.out.println("修改成功！");
            return true;
        }else {
            System.out.println("修改失败！");
            return false;
        }

    }


    // 根据ID删除订单
    @DeleteMapping("/orders/{ordersIds}")
    public R derOrders(@PathVariable("ordersIds") List<String> ordersIds){
        System.out.println("----------delorders------------");
        System.out.println(ordersIds);
        for (String orderId:ordersIds) {
            int flag = orderService.delOrderByOrderId(Integer.parseInt(orderId));
            if(flag==0){
                System.out.println(orderId+":删除失败！");
                return R.error();
            }
            System.out.println(orderId+":删除成功!");
        }
        return R.ok();
    }

}
