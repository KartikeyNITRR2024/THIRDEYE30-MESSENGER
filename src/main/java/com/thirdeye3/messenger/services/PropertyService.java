package com.thirdeye3.messenger.services;

public interface PropertyService {

	void fetchProperties();

	Long getSizeToLoadStock();

	Long getMaximumMessageLength();

	Long getMaximumMessageReadFromMessageBroker();

}
