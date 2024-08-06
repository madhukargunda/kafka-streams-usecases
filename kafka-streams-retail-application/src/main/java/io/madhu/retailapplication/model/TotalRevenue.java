package io.madhu.retailapplication.model;

import java.math.BigDecimal;

public record TotalRevenue(String locationId,
                           Integer runnuingOrderCount,
                           BigDecimal runningRevenue) {
}
