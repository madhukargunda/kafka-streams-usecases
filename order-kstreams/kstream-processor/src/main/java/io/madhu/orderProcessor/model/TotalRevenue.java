package io.madhu.orderProcessor.model;

import java.math.BigDecimal;

public record TotalRevenue(String locationId,
                           Integer runningOrderCount,
                           BigDecimal runningRevenue) {
}
