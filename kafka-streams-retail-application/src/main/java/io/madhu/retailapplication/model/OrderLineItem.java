/**
 * Author: Madhu
 * User:madhu
 * Date:9/7/24
 * Time:11:32 AM
 * Project: kafka-streams-retail-application
 */

package io.madhu.retailapplication.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderLineItem {

    private String item;
    private Integer count;
    private BigDecimal amount;
}
