/**
 * Author: Madhu
 * User:madhu
 * Date:23/7/24
 * Time:10:21 AM
 * Project: kafka-streams-retail-application
 */

package io.madhu.retailapplication.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@Configuration
@Slf4j
public class RetailsStreamConfiguration {

    @Bean
    public StreamsBuilder streamsBuilder(){
        return new StreamsBuilder();
    }

}
