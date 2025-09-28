package com.thirdeye3.messenger.services;

public interface PriceChangeService {

	void fetchNewChanges();

	void createMessageForUsers();

	void clearMap();

}
