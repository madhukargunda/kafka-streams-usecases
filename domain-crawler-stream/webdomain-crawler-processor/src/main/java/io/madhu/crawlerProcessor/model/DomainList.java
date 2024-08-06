/**
 * Author: Madhu
 * User:madhu
 * Date:30/7/24
 * Time:5:21 PM
 * Project: webdomain-crawler-stream
 */

package io.madhu.crawlerProcessor.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class DomainList  implements Serializable {

    private List<WebDomain> webDomains;
}
