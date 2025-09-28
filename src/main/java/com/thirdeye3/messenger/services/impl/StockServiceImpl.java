package com.thirdeye3.messenger.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thirdeye3.messenger.dtos.Response;
import com.thirdeye3.messenger.dtos.Stock;
import com.thirdeye3.messenger.exceptions.PropertyFetchException;
import com.thirdeye3.messenger.exceptions.StockException;
import com.thirdeye3.messenger.externalcontrollers.StockManagerClient;
import com.thirdeye3.messenger.services.PropertyService;
import com.thirdeye3.messenger.services.StockService;

@Service
public class StockServiceImpl implements StockService {
	
	private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

	@Autowired
	private StockManagerClient stockManager;
	
	@Autowired
	private PropertyService propertyService;
	
    private Map<Long, Stock> stocks = null;

    @Override
    public void fetchStocks() {
        Response<Long> response = stockManager.getStockSize();
        if (response.isSuccess()) {
            Long totalStockSize = response.getResponse();
            Long totalPages = (long) Math.ceil((double) totalStockSize / propertyService.getSizeToLoadStock());
            logger.info("Total stocks: " + totalStockSize + " | Fetching in " + totalPages + " pages...");
            stocks = new HashMap<>();
            for (long page = 0; page < totalPages; page++) {
                Response<List<Stock>> response1 = stockManager.getStocks(page, propertyService.getSizeToLoadStock());
                if(response1.isSuccess())
                {
                	List<Stock> stockList = response1.getResponse();
	                for (Stock stock : stockList) {
	                    stocks.put(stock.getUniqueId(), stock);
	                }
                }
                else
                {
                	logger.error("Unable to fetch stock of page "+page);
                }
            }
            logger.info("Fetched " + stocks.size() + " stocks successfully.");
        } else {
        	stocks = new HashMap<>();
            throw new StockException("Unable to fetch properties from Property Manager");
        }
    }

    @Override
    public Stock getStockById(Long id) {
    	if(stocks == null)
    	{
    		fetchStocks();
    	}
        return stocks.get(id);
    }
}
