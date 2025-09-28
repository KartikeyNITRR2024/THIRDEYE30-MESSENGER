package com.thirdeye3.messenger.externalcontrollers;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.thirdeye3.messenger.dtos.Response;
import com.thirdeye3.messenger.configs.FeignConfig;
import com.thirdeye3.messenger.dtos.Message;
import com.thirdeye3.messenger.dtos.PriceChange;

@FeignClient(
		name = "THIRDEYE30-MESSAGEBROKER",
		configuration = FeignConfig.class
)
public interface MessageBrokerClient {

    @GetMapping("/mb/message/multiple/{topicname}/{topickey}/{count}")
    Response<List<Message<PriceChange>>> getMessages(
        @PathVariable("topicname") String topicName,
        @PathVariable("topickey") String topicKey,
        @PathVariable("count") Long count
    );
    
    @PostMapping("/mb/message/multiple/{topicname}/{topickey}")
    Response<String> setMessages(
            @PathVariable("topicname") String topicName,
            @PathVariable("topickey") String topicKey,
            @RequestBody Object messages
    );
}

