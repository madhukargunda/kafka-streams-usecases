/**
 * Author: Madhu
 * User:madhu
 * Date:30/7/24
 * Time:4:56 PM
 * Project: webdomain-crawler-stream
 */

package io.madhu.webDomainCrawler.controller;

import io.madhu.webDomainCrawler.service.DomainCrawlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
@Slf4j
public class DomainCrawlerController {

    @Autowired
    private DomainCrawlerService domainCrawlerService;

    @GetMapping("/lookup/{name}")
    public ResponseEntity<?> lookup(@PathVariable("name") final String name) {
        log.info("Triggering the Lookup service");
        domainCrawlerService.crawl(name);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }
}
