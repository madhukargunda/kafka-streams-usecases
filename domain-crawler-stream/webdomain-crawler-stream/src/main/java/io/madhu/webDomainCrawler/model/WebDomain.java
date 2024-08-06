/**
 * Author: Madhu
 * User:madhu
 * Date:30/7/24
 * Time:4:46 PM
 * Project: webdomain-crawler-stream
 */

package io.madhu.webDomainCrawler.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class WebDomain implements Serializable {

    String domain;
    String create_date;
    String updated_date;
    String country;
    boolean isDead;
    String A;
    String NS;
    String MX;
    String TXT;
}
