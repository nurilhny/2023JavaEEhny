package com.hny.service;


import com.hny.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "USER-SERVICE")
public interface CommonFeignService {

    @GetMapping("/users/{userId}")
    R getUserByUserId(@PathVariable("userId") int userId);

    @GetMapping("/users/account/{account}")
    Integer getUserIdByAccount(@PathVariable("account") String username);


}
