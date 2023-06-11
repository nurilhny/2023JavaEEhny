package com.hny.service.impl;

import com.hny.entity.SecurityUser;
import com.hny.entity.User;
import com.hny.service.CommonFeignService;
import com.hny.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    CommonFeignService commonFeignService;

    @Autowired
    UserService userService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        System.out.println(username);
        Integer userId = userService.getUserIdByAccount(username);

        User user  = userService.getUserByUserId(userId);

        System.out.println("-------------进来咯-------------");
        System.out.println(userId);
        System.out.println(username);
        System.out.println(user);
        if(user==null){
            throw new UsernameNotFoundException("用户不存在！");
        }

        String userType = user.getUserType();
        List<String> permissions = new ArrayList<>();

        // 根据用户查询用户权限
        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurUser(user);

        // 判断类型是否为空
        if(userType==null){
            return securityUser;
        }
        System.out.println("----------------------权限:"+userType);

        // 判断类型，设置权限
        if(userType.equals("manager")){
            permissions.add("manager");
            securityUser.setPermissionList(permissions);
        }else if(userType.equals("user")){
            permissions.add("user");
            securityUser.setPermissionList(permissions);
        }else if(userType.equals("courier")){
            permissions.add("courier");
            securityUser.setPermissionList(permissions);
        }


        return securityUser;
    }
}
