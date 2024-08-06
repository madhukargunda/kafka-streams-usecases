/**
 * Author: Madhu
 * User:madhu
 * Date:5/9/24
 * Time:9:45 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.controller;

import io.madhu.toyataSalesAnalytics.service.CarSalesAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ToyotaCarSalesController {

    @Autowired
    CarSalesAnalysisService carSalesAnalysisService;

    @GetMapping("/cars/sales/{state}")
    public ResponseEntity<?> carSalesCountByStateWide(@PathVariable("state") String stateName) {
        log.info("The CarType {} ", stateName);
        return ResponseEntity.ok(carSalesAnalysisService.getSalesCountByStateName(stateName));
    }

    @GetMapping("/cars/sales")
    public ResponseEntity<?> carSalesCount() {
        return ResponseEntity.ok(carSalesAnalysisService.getSalesCount());
    }
}