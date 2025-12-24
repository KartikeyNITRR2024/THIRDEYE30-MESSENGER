package com.thirdeye3.messenger.services;

public interface PriceChangeService {

	Boolean fetchNewChanges();

	void createMessageForUsers();

	void clearMap();

}
