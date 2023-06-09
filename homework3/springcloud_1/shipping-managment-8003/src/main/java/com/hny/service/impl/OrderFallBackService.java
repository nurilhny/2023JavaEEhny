package com.hny.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hny.entity.User;
import com.hny.service.OrderFeignService;
import com.hny.utils.R;
import org.springframework.stereotype.Component;

import java.util.HashMap;


// 该类统一处理对应feign的服务降级
@Component
public class OrderFallBackService implements OrderFeignService {
    @Override
    public R getUserByUserId(Integer userId) throws JsonProcessingException {
        R r = R.error();
        User user = new User();
        user.setUserId(0);
        user.setUserName("没得到的用户");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);
        r.getData().put("user",json);
        return r;
    }
}
