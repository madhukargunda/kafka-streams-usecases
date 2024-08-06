/**
 * Author: Madhu
 * User:madhu
 * Date:10/9/24
 * Time:3:12 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentDTO {

    private String name;
    private Long count;
    private String fullName;

}


