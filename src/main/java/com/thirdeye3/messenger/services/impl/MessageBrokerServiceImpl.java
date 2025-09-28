package com.thirdeye3.messenger.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye3.messenger.configs.MessageBrokerConfig;
import com.thirdeye3.messenger.dtos.Message;
import com.thirdeye3.messenger.dtos.PriceChange;
import com.thirdeye3.messenger.dtos.Response;
import com.thirdeye3.messenger.exceptions.MessageBrokerException;
import com.thirdeye3.messenger.externalcontrollers.MessageBrokerClient;
import com.thirdeye3.messenger.services.MessageBrokerService;
import com.thirdeye3.messenger.services.PropertyService;


@Service
public class MessageBrokerServiceImpl implements MessageBrokerService {
	
    private static final Logger logger = LoggerFactory.getLogger(MessageBrokerServiceImpl.class);
    
    @Autowired
    private MessageBrokerConfig messageBrokerConfig;
    
    @Autowired 
    private MessageBrokerClient messageBroker;
    
    @Autowired 
    private PropertyService propertyService;

    @Override
	public List<Message<PriceChange>> getMessage(String topicName)
	{
		List<Message<PriceChange>> messages = new ArrayList<>();
		if(!messageBrokerConfig.getTopics().containsKey(topicName))
    	{
    		throw new MessageBrokerException("Does not have any topic with topic name "+topicName);
    	}
		try {
    		Response<List<Message<PriceChange>>> response = messageBroker.getMessages(topicName, messageBrokerConfig.getTopics().get(topicName).getTopicKey(), propertyService.getMaximumMessageReadFromMessageBroker());
    		if (response.isSuccess()) {
    			messages = response.getResponse();
                logger.info("Successfully received messages from message broker with topic name "+topicName);
            }
    		else
    		{
    		    throw new MessageBrokerException("Failed to recive messages from message broker with topic name "+topicName+" "+response.getErrorMessage());
    		}
    	} catch (Exception e) {
    		//throw new MessageBrokerException("Failed to recive messages from message broker with topic name "+topicName);
    		logger.info("Failed to read data");
        }
		return messages;
	}
    
    @Override
    public void sendMessages(String topicName, Object messages)
    {
    	if(!messageBrokerConfig.getTopics().containsKey(topicName))
    	{
    		throw new MessageBrokerException("Does not have any topic with topic name "+topicName);
    	}
    	try {
    		Response<String> response = messageBroker.setMessages(topicName, messageBrokerConfig.getTopics().get(topicName).getTopicKey(), messages);
    		if (response.isSuccess()) {
                logger.info("Successfully send messages to message broker with topic name "+topicName);
            }
    		else
    		{
    		    throw new MessageBrokerException("Failed to send messages to message broker with topic name "+topicName+" "+response.getErrorMessage());
    		}
    	} catch (Exception e) {
    		throw new MessageBrokerException("Failed to send messages to message broker with topic name "+topicName+" "+e.getMessage());
        }
    }
}
