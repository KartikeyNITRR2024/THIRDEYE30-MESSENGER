package com.thirdeye3.messenger.externalcontrollers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.thirdeye3.messenger.dtos.Response;
import com.thirdeye3.messenger.configs.FeignConfig;

@FeignClient(
	    name = "${spring.application.name}", 
	    url = "${self.url:}",
	    configuration = FeignConfig.class
)
public interface SelfClient {
    @GetMapping("/api/statuschecker/{id}/{code}")
	Response<String> statusChecker(@PathVariable("id") Integer id, @PathVariable("code") String code);
}