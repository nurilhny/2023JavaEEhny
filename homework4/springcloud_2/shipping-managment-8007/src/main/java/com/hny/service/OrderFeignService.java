package com.hny.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hny.config.FeignConfig;
import com.hny.service.impl.OrderFallBackService;
import com.hny.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "USER-SERVICE", fallback = OrderFallBackService.class, configuration = FeignConfig.class)
public interface OrderFeignService {

    @GetMapping("/users/{userId}")
    R getUserByUserId(@PathVariable("userId") Integer userId) throws JsonProcessingException;
}
