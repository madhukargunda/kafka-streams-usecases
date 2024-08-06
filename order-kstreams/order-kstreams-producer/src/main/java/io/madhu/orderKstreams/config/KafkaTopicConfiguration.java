/**
 * Author: Madhu
 * User:madhu
 * Date:4/8/24
 * Time:11:00 AM
 * Project: order-kstreams-producer
 */

package io.madhu.orderKstreams.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    @Bean
    public NewTopic orders() {
        return TopicBuilder
                .name("orders")
                .partitions(2)
                .build();
    }

    @Bean
    public NewTopic stores() {
        return TopicBuilder.
                name("stores")
                .partitions(2)
                .build();
    }
}
