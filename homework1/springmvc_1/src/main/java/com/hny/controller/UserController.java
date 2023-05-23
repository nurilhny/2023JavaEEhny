package com.hny.controller;

import com.hny.bean.User;
import com.hny.service.UserService;
import com.hny.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // 登录验证
    @PostMapping("/login")
    public String handleLogin(User user, HttpSession session){
        System.out.println(user.getPassword());
        // 获取用户类型
        String userType = userService.getUserTypeByAccount(user.getAccount());

        // md5加密
        String password = MD5Utils.getPWD(user.getPassword());
        // 获取数据库密码
        String dbPassword = userService.getPasswordByAccount(user.getAccount());

        int userId = userService.getUserIdByAccount(user.getAccount());

        System.out.println(password);
        System.out.println(dbPassword);

        // 获取完整user记录
        User fullUser = userService.getUserByUserId(userId);

        // 设置验证对象
        User deUser = new User();
        deUser.setAccount(user.getAccount());
        deUser.setPassword(password);
        deUser.setUserType(userType);
        deUser.setUserId(userId);
        deUser.setAddress(fullUser.getAddress());


        // 验证成功
        if(password.equals(dbPassword)){
            session.setAttribute("_user",deUser);
            if(userType.equals("user")){
                return "redirect:/order/user";

            }else if(userType.equals("courier")){
                return "redirect:/order/courier";
            }
            else if(userType.equals("manager")){
                return "redirect:/manager";
            }

            return "hello";
        }

        return "redirect:/";
    }

    // 跳转注册界面
    @GetMapping("/register")
    public String handleRegister(){
        return "register";
    }

    // 注册
    @PostMapping("/register")
    public String handleRegisterToLogin(User user){
        String account = userService.getPasswordByAccount(user.getAccount());
        if(account == null){
            // 加密
            user.setPassword(MD5Utils.getPWD(user.getPassword()));
            int flag = userService.addAccount(user);
           if(flag==1){
               System.out.println("注册成功！");
               return "redirect:/";
           }
            System.out.println("注册失败！");
            return "redirect:/register";
        }
        System.out.println("注册失败！");
        return "redirect:/register";

    }

    // 管理员页面
    @GetMapping("/manager")
    public ModelAndView handleManagerPage(HttpSession session){
        User user = (User)session.getAttribute("_user");
        ModelAndView modelAndView = new ModelAndView();
        List<User> users = userService.getAllUsers();
        modelAndView.addObject("users",users);
        modelAndView.addObject("_user",user);
        modelAndView.setViewName("manager");
        return modelAndView;

    }

    // 删除用户
    @GetMapping("/manager/delete")
    public String delUser(@RequestParam("userId") int userId){

        int flag = userService.delUserByUserId(userId);

        if(flag==1){
            System.out.println("删除成功！");
        }else {
            System.out.println("删除失败！");
        }

        return "redirect:/manager";

    }

}
