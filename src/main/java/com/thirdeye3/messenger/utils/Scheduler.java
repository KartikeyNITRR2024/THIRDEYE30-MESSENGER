package com.thirdeye3.messenger.utils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.thirdeye3.messenger.dtos.Response;
import com.thirdeye3.messenger.externalcontrollers.SelfClient;
import com.thirdeye3.messenger.services.PriceChangeService;
@Component
public class Scheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
	
    @Autowired
    SelfClient selfClient;
    
    @Autowired
    TimeManager timeManager;
    
    @Autowired
    Initiatier initiatier;
    
    @Autowired
    PriceChangeService priceChangeService;
    
    @Value("${thirdeye.uniqueId}")
    private Integer uniqueId;

    @Value("${thirdeye.uniqueCode}")
    private String uniqueCode;
	
	@Scheduled(fixedRate = 30000)
    public void checkStatusTask() {
        Response<String> response = selfClient.statusChecker(uniqueId, uniqueCode);
        logger.info("Status check response is {}", response.getResponse());
    }
	
    @Scheduled(cron = "${thirdeye.scheduler.cronToRefreshData}", zone = "${thirdeye.timezone}")
    public void runToRefreshdata() {
        try {
            initiatier.refreshMemory();
            logger.info("🔄 Data refreshed at {}", timeManager.getCurrentTime());
        } catch (Exception e) {
            logger.error("❌ Failed to refresh data at {}: {}", timeManager.getCurrentTime(), e.getMessage());
        }
    }
    
    @Scheduled(fixedRateString = "${thirdeye.message.broker.read.rate}")
    public void readMessagesFromMessageBroker() {
    	priceChangeService.fetchNewChanges();
        logger.info("Going to read data from message broker");
    }
    
    @Scheduled(fixedRateString = "${thirdeye.message.sendgap}")
    public void sendMessageToUser() {
    	priceChangeService.createMessageForUsers();
        logger.info("Going to send message to user");
    }

}
