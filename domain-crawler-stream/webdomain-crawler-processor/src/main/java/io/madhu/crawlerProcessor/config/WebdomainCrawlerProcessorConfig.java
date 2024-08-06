/**
 * Author: Madhu
 * User:madhu
 * Date:30/7/24
 * Time:6:54 PM
 * Project: webdomain-crawler-processor
 */

package io.madhu.crawlerProcessor.config;

import io.madhu.crawlerProcessor.constants.KafkaStreamProperties;
import io.madhu.crawlerProcessor.model.WebDomain;
import io.madhu.crawlerProcessor.serdes.CustomSerdes;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Map;

@Configuration
@Slf4j
@Profile("webdomain-processor")
public class WebdomainCrawlerProcessorConfig {

    private static final String WEB_DOMAIN_TOPIC_NAME = "web-domains";

    @Autowired
    private KafkaStreamProperties kafkaStreamProperties;

    @Bean
    public Map<String, Object> configMap() {
        Map<String, Object> props = kafkaStreamProperties.toMap();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "web-domains-streaming-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new JsonSerde<WebDomain>().getClass().getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "io.madhu.webDomainCrawler.model");
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);
        return props;
    }

    @Bean
    public StreamsConfig streamsConfig() {
        return new StreamsConfig(configMap());
    }

    @Bean
    StreamsBuilder streamsBuilder() {
        return new StreamsBuilder();
    }

    @Bean
    public Topology topology() {
        log.info("Topology processor executed");
        //1 .Create the KStream object
        KStream<String, WebDomain> wordsKStream = streamsBuilder()
                .stream(WEB_DOMAIN_TOPIC_NAME, Consumed.with(Serdes.String(), CustomSerdes.webDomain())
                        .withOffsetResetPolicy(Topology.AutoOffsetReset.EARLIEST));
        // 2.Print the Stream
        wordsKStream.print(Printed.<String, WebDomain>toSysOut().withName("Debugging"));
        return streamsBuilder().build();
    }

    @Bean
    public KafkaStreams wordsCountKafkaStream() {
        KafkaStreams streams = new KafkaStreams(topology(), streamsConfig());
        // Uses the isCleanupOnStart property to conditionally clean up state before starting the streams.
        if (kafkaStreamProperties.isCleanUpOnStart()) {
            streams.cleanUp();
        }
        streams.start(); //We can call this in service method aswell.
        return streams;
    }
}
