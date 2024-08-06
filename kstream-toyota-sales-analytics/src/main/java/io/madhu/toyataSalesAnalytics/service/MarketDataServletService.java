/**
 * Author: Madhu
 * User:madhu
 * Date:10/9/24
 * Time:12:58 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.service;


import io.madhu.toyataSalesAnalytics.gateway.MarketServletGateWay;
import io.madhu.toyataSalesAnalytics.dto.MarketDataServletDTO;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class MarketDataServletService {

    @Autowired
    private MarketServletGateWay marketServletGateWay;

    public MarketDataServletDTO getMarketData() throws ServletException, IOException {
        log.info("In Service Layer......... {}");
        return marketServletGateWay.getMarketData();
    }
}
