package com.thirdeye3.messenger.utils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thirdeye3.messenger.services.GroupService;
import com.thirdeye3.messenger.services.PriceChangeService;
import com.thirdeye3.messenger.services.PropertyService;
import com.thirdeye3.messenger.services.StockService;
import com.thirdeye3.messenger.services.GroupService;

import jakarta.annotation.PostConstruct;

@Component
public class Initiatier {
	
    private static final Logger logger = LoggerFactory.getLogger(Initiatier.class);
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private StockService stockService;
    
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private PriceChangeService priceChangeService;
	
    @Value("${thirdeye.priority}")
    private Integer priority;
    
	@PostConstruct
    public void init() throws Exception{
        logger.info("Initializing Initiatier...");
    	TimeUnit.SECONDS.sleep(priority * 3);
        propertyService.fetchProperties();
        stockService.fetchStocks();
        groupService.fetchGroups();
        logger.info("Initiatier initialized.");
    }
	
	public void refreshMemory()
	{
		logger.info("Going to refersh memory...");
		priceChangeService.clearMap();
		logger.info("Memory refreshed.");
	}

}
