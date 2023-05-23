package com.hny.interceptor;

import com.hny.bean.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class FirstInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User)request.getSession().getAttribute("_user");
        if(user == null){
            response.sendRedirect("redirect:/");
            System.out.println("拒绝访问！！！！");
            return false;
        }else {
            System.out.println("允许访问！！");
            return true;
        }

    }


}
