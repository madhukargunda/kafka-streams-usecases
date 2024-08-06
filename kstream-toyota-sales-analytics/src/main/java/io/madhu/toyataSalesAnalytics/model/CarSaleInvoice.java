/**
 * Author: Madhu
 * User:madhu
 * Date:28/7/24
 * Time:5:54 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class CarSaleInvoice implements Serializable {

    private String transactionId;
    private String make;
    private String model;
    private String year;
    private String saleTimestamp;
    private String dealerId;
    private String dealerName;
    private String state;
    private String price;
}
