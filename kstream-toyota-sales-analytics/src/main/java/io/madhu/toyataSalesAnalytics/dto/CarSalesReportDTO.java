/**
 * Author: Madhu
 * User:madhu
 * Date:8/9/24
 * Time:9:45 AM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarSalesReportDTO {
    private String stateName;
    private Long count;
}
