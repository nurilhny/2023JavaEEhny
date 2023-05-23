package com.hny.controller;

import com.hny.bean.Order;
import com.hny.bean.Product;
import com.hny.bean.Site;
import com.hny.bean.User;
import com.hny.service.OrderService;
import com.hny.service.ProductService;
import com.hny.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    SiteService siteService;

    @GetMapping("/order/user")
    public ModelAndView getOrdersByUserId(HttpSession session) {
        User user = (User)session.getAttribute("_user");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orders",orderService.getOrdersByUserId(user.getUserId()));
        modelAndView.addObject("user",user);
        modelAndView.setViewName("user");
        return modelAndView;

    }


    @GetMapping("/order/courier")
    public ModelAndView getOrdersBySiteAddress(HttpSession session) {
        User user = (User)session.getAttribute("_user");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orders",orderService.getOrdersByAddress(user.getAddress()));
        modelAndView.addObject("user",user);
        modelAndView.setViewName("courier");
        return modelAndView;

    }

    // 跳转添加页面
    @GetMapping("/order/add")
    public ModelAndView getAddPage() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products",productService.getAllProducts());
        modelAndView.setViewName("addUser");
        return modelAndView;

    }

    @PostMapping("/order/user/add")
    public String addOrder(Order order, HttpSession session) {
        System.out.println("----------------");
        System.out.println(order);
        System.out.println("----------------");
        User user = (User)session.getAttribute("_user");

        order.setUserId(user.getUserId());
        Product product = productService.getProductByName(order.getProductName());

        order.setSiteId(product.getSiteId());
        order.setSrcAddress(product.getSrcAddress());
        order.setTotalPrice(product.getPrice()*order.getQuantity());

        System.out.println("----------------");
        System.out.println(order);
        System.out.println("----------------");

        int flag = orderService.addOrder(order);

        if(flag==1){
            System.out.println("添加成功！");
        }else {
            System.out.println("添加失败！");
        }
        return "redirect:/order/user";

    }

    @GetMapping("/order/detail")
    public ModelAndView orderDetail(@RequestParam("orderId") int orderId){
        ModelAndView modelAndView = new ModelAndView();

        Order order = orderService.getOrderDetailByOrderId(orderId);
        modelAndView.addObject("order",order);
        modelAndView.setViewName("orderDetail");
        return modelAndView;
    }

    @GetMapping("/order/update")
    public String orderTypeUpdate(@RequestParam("orderId") int orderId,@RequestParam("orderStatus") String orderStatus,HttpSession session){

        User user =  (User)session.getAttribute("_user");

        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatus(orderStatus);

        String userType = user.getUserType();
        int flag = orderService.updateOrderStatus(order);

        if(flag==1){
            System.out.println("修改成功！");
        }else {
            System.out.println("修改失败！");
        }

        if(userType.equals("user")){
            return "redirect:/order/user";
        }else if(userType.equals("manager")){
            return "redirect:/order/manager";
        }else if(userType.equals("courier")){
            return "redirect:/order/courier";
        }
        return "redirect:/";
    }

    @GetMapping("/order/changeSite")
    public ModelAndView changeSitePage(@RequestParam("orderId") int orderId,HttpSession session){
        User user = (User)session.getAttribute("_user");
        List<Site> sites = siteService.getAllSites();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orderId",orderId);
        modelAndView.addObject("user",user);
        modelAndView.addObject("sites",sites);
        modelAndView.setViewName("changeSite");
        return modelAndView;
    }

    @PostMapping("/order/courier/changeSite")
    @Transactional
    public String changeSite(Order order, HttpServletRequest request){
        System.out.println(request.getParameter("orderId"));

        System.out.println("--------------------");
        System.out.println(order);
        System.out.println("--------------------");
        // 修改站点
        int flag1 = orderService.updateCourierIdByOrderId(order);

        int flag2 = orderService.updateSiteIdByOrderId(order);

        if(flag1==1&&flag2==1){
            System.out.println("修改成功！");
        }else {
            System.out.println("修改失败！");
        }

        return "redirect:/order/courier";
    }

    @GetMapping("/order/quit")
    public String quitSystem(HttpSession session) {
        session.invalidate();
        return "redirect:/";

    }
}
