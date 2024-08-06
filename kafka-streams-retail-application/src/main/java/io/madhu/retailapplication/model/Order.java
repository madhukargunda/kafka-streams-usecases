/**
 * Author: Madhu
 * User:madhu
 * Date:9/7/24
 * Time:10:39 AM
 * Project: kafka-streams-retail-application
 */

package io.madhu.retailapplication.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private Integer orderId;
    private String locationId;
    private BigDecimal finaAmount;
    private OrderType orderType;
    private List<OrderLineItem> orderLineItemList;
    private LocalDateTime localDateTime;
}
