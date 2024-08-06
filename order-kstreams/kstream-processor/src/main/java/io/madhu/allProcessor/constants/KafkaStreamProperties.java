/**
 * Author: Madhu
 * User:madhu
 * Date:15/7/24
 * Time:1:25 PM
 * Project: order-kstream-processor
 */

package io.madhu.allProcessor.constants;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.kafka.streams")
@Configuration
@Slf4j
public class KafkaStreamProperties {

    private String applicationId;
    private String bootstrapServers;
    private String defaultKeySerde;
    private String defaultValueSerde;
    private int numStreamThreads;
    private boolean cleanUpOnStart;
    private String stateDir;
    private int replicationFactor;

    public Map<String, Object> toMap() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, defaultKeySerde);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, defaultValueSerde);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, numStreamThreads);
      //  props.put(StreamsConfig.CLEANUP_POLICY_CONFIG, cleanUpOnStart);
      //  props.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
        return props;
    }

    // Getters and setters
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getDefaultKeySerde() {
        return defaultKeySerde;
    }

    public void setDefaultKeySerde(String defaultKeySerde) {
        this.defaultKeySerde = defaultKeySerde;
    }

    public String getDefaultValueSerde() {
        return defaultValueSerde;
    }

    public void setDefaultValueSerde(String defaultValueSerde) {
        this.defaultValueSerde = defaultValueSerde;
    }

    public int getNumStreamThreads() {
        return numStreamThreads;
    }

    public void setNumStreamThreads(int numStreamThreads) {
        this.numStreamThreads = numStreamThreads;
    }

    public boolean isCleanUpOnStart() {
        return cleanUpOnStart;
    }

    public void setCleanUpOnStart(boolean cleanUpOnStart) {
        this.cleanUpOnStart = cleanUpOnStart;
    }

    public String getStateDir() {
        return stateDir;
    }

    public void setStateDir(String stateDir) {
        this.stateDir = stateDir;
    }

    public int getReplicationFactor() {
        return replicationFactor;
    }

    public void setReplicationFactor(int replicationFactor) {
        this.replicationFactor = replicationFactor;
    }
}

