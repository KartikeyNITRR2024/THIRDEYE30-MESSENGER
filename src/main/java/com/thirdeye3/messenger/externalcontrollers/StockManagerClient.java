package com.thirdeye3.messenger.externalcontrollers;

import com.thirdeye3.messenger.configs.FeignConfig;
import com.thirdeye3.messenger.dtos.Response;
import com.thirdeye3.messenger.dtos.Stock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
		name = "THIRDEYE30-STOCKMANAGER",
		configuration = FeignConfig.class
)
public interface StockManagerClient {

    @GetMapping("/sm/stocks/{page}/{size}")
    Response<List<Stock>> getStocks(
            @PathVariable("page") long page,
            @PathVariable("size") long size
    );

    @GetMapping("/sm/stocks/size")
    Response<Long> getStockSize();
}

