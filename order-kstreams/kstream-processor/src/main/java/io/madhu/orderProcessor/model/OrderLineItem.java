package io.madhu.orderProcessor.model;

import java.math.BigDecimal;

public record OrderLineItem(
        String item,
        Integer count,
        BigDecimal amount) {
}
