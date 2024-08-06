/**
 * Author: Madhu
 * User:madhu
 * Date:9/9/24
 * Time:11:25 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateCountDTO {
    private String stateName;
    private Long count;
}
