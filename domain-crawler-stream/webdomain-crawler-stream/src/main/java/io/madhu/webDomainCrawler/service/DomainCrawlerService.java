/**
 * Author: Madhu
 * User:madhu
 * Date:30/7/24
 * Time:4:57 PM
 * Project: webdomain-crawler-stream
 */

package io.madhu.webDomainCrawler.service;

import io.madhu.webDomainCrawler.model.WebDomain;
import io.madhu.webDomainCrawler.model.DomainList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class DomainCrawlerService {

    private static final String WEB_DOMAIN_TOPIC_NAME = "web-domains";

    @Autowired
    KafkaTemplate<String, WebDomain> kafkaTemplate;

    @Value("${spring.application.domainUrl}")
    String domainCrawlURL;

    public void crawl(String name) {
        log.info("Web Crawling.........");

        Mono<DomainList> domainListMono = WebClient.create(domainCrawlURL)
                .get()
                .uri(uriBuilder -> uriBuilder.path("/v1/domains/search")
                       .queryParam("domain", name)
                        .queryParam("zone", "com").build())
                //.uri("https://api.domainsdb.info/v1/domains/search?domain=facebook&zone=com")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(DomainList.class);

        domainListMono.subscribe(domainList -> {
            domainList.getDomains().forEach(webDomain -> {
                log.info("Publishing Domain to Kafka {}", webDomain);;
                CompletableFuture<SendResult<String, WebDomain>> results = kafkaTemplate.send(WEB_DOMAIN_TOPIC_NAME, webDomain);
                results.whenComplete((r,d) -> {
                  log.info("Domain Object {} published successfully {}",d,r.toString());
                });
            });
        });
    }

}
