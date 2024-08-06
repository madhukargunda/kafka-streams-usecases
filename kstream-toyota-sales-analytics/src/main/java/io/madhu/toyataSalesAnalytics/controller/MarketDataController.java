/**
 * Author: Madhu
 * User:madhu
 * Date:10/9/24
 * Time:12:56 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.controller;

import io.madhu.toyataSalesAnalytics.dto.MarketDataServletDTO;
import io.madhu.toyataSalesAnalytics.service.MarketDataServletService;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
public class MarketDataController {

    @Autowired
    private MarketDataServletService marketDataServletService;

    @GetMapping(value = "/market/currency",produces = MediaType.APPLICATION_JSON_VALUE)
    public MarketDataServletDTO getMarketData() throws ServletException, IOException {
        log.info("request is received MarketServletController ");
        return marketDataServletService.getMarketData();
    }
}
