/**
 * Author: Madhu
 * User:madhu
 * Date:15/7/24
 * Time:1:25 PM
 * Project: order-kstream-processor
 */

package io.madhu.toyataSalesAnalytics.constants;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.kafka.streams")
@Configuration
@Slf4j
@Component
@Data
public class CarSalesStreamProperties {

    private String applicationId;
    private String bootstrapServers;
    private String defaultKeySerde;

    @Override
    public String toString() {
        return "KafkaStreamProperties{" +
                "applicationId='" + applicationId + '\'' +
                ", bootstrapServers='" + bootstrapServers + '\'' +
                ", defaultKeySerde='" + defaultKeySerde + '\'' +
                ", defaultValueSerde='" + defaultValueSerde + '\'' +
                ", numStreamThreads=" + numStreamThreads +
                ", cleanUpOnStart=" + cleanUpOnStart +
                ", stateDir='" + stateDir + '\'' +
                ", replicationFactor=" + replicationFactor +
                '}';
    }

    private String defaultValueSerde;
    private Integer numStreamThreads;
    private boolean cleanUpOnStart;
    private String stateDir;
    private Integer replicationFactor;
    private Long commitInterval;

    public Map<String, Object> toMap() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, defaultKeySerde);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, defaultValueSerde);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, numStreamThreads);
      //  props.put(StreamsConfig.CLEANUP_POLICY_CONFIG, cleanUpOnStart);
        props.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,commitInterval);
        return props;
    }
}

