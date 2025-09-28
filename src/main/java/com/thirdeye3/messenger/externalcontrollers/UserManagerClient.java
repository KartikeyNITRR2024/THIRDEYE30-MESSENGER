package com.thirdeye3.messenger.externalcontrollers;

import com.thirdeye3.messenger.dtos.Response;
import com.thirdeye3.messenger.configs.FeignConfig;
import com.thirdeye3.messenger.dtos.Group;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(
		name = "THIRDEYE30-USERMANAGER",
		configuration = FeignConfig.class
)
public interface UserManagerClient {
	
    @GetMapping("/um/admin/threshold-groups/active/2")
    Response<Map<Long, Group>> getAllGroups();
}

