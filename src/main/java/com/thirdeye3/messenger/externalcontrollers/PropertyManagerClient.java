package com.thirdeye3.messenger.externalcontrollers;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.thirdeye3.messenger.configs.FeignConfig;
import com.thirdeye3.messenger.dtos.Response;

@FeignClient(
		name = "THIRDEYE30-PROPERTYMANAGER",
		configuration = FeignConfig.class
)
public interface PropertyManagerClient {
    
    @GetMapping("/pm/properties")
    Response<Map<String, Object>> getProperties();
}
