package com.thirdeye3.messenger.services.impl;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye3.messenger.services.PropertyService;
import com.thirdeye3.messenger.dtos.Response;
import com.thirdeye3.messenger.exceptions.PropertyFetchException;
import com.thirdeye3.messenger.externalcontrollers.PropertyManagerClient;

@Service
public class PropertyServiceImpl implements PropertyService {
	private static final Logger logger = LoggerFactory.getLogger(PropertyServiceImpl.class);

    @Autowired
    private PropertyManagerClient propertyManager;

    private Map<String, Object> properties = null;
    private Long sizeToLoadStock = null;
    private Long maximumMessageLength = null;
    private Long maximumMessageReadFromMessageBroker = null;


    @Override
    public void fetchProperties() {
        Response<Map<String, Object>> response = propertyManager.getProperties();
        if (response.isSuccess()) {
            properties = response.getResponse();
            sizeToLoadStock = ((Number) properties.getOrDefault("SIZE_TO_LOAD_STOCK", 50L)).longValue();
            maximumMessageLength = ((Number) properties.getOrDefault("MAXIMUM_MESSAGE_LENGTH", 300L)).longValue();
            maximumMessageReadFromMessageBroker = ((Number) properties.getOrDefault("MAXIMUM_MESSAGE_READ_FROM_MESSAGE_BROKER", 50L)).longValue();
            logger.info("Request {}, {}, {}", properties, sizeToLoadStock, maximumMessageLength);
        } else {
            properties = new HashMap<>();
            sizeToLoadStock=50L;
            maximumMessageLength=300L;
            maximumMessageReadFromMessageBroker=50L;
            logger.error("Failed to fetch properties");
            throw new PropertyFetchException("Unable to fetch properties from Property Manager");
        }
    }

    @Override
	public Long getSizeToLoadStock() {
    	if(sizeToLoadStock == null)
    	{
    		fetchProperties();
    	}
		return sizeToLoadStock;
	}

    @Override
	public Long getMaximumMessageLength() {
    	if(maximumMessageLength == null)
    	{
    		fetchProperties();
    	}
		return maximumMessageLength;
	}

    @Override
	public Long getMaximumMessageReadFromMessageBroker() {
    	if(maximumMessageReadFromMessageBroker == null)
    	{
    		fetchProperties();
    	}
		return maximumMessageReadFromMessageBroker;
	}
    
    
    
    
}
