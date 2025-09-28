package com.thirdeye3.messenger.services;

import java.util.List;

import com.thirdeye3.messenger.dtos.Message;
import com.thirdeye3.messenger.dtos.PriceChange;

public interface MessageBrokerService {

	List<Message<PriceChange>> getMessage(String string);

	void sendMessages(String topicName, Object messages);

}
