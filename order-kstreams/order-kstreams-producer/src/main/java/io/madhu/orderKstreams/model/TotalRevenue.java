package io.madhu.orderKstreams.model;

import java.math.BigDecimal;

public record TotalRevenue(String locationId,
                           Integer runningOrderCount,
                           BigDecimal runningRevenue) {
}
