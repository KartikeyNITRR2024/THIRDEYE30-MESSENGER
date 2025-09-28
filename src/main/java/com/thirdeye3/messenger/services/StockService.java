package com.thirdeye3.messenger.services;

import com.thirdeye3.messenger.dtos.Stock;

public interface StockService {

	void fetchStocks();

	Stock getStockById(Long id);

}
