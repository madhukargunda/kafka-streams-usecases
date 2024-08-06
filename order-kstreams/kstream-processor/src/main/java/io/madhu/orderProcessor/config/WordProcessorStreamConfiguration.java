/**
 * Author: Madhu
 * User:madhu
 * Date:14/7/24
 * Time:10:02 PM
 * Project: order-kstream-processor
 */

package io.madhu.orderProcessor.config;

import io.madhu.orderProcessor.constants.KafkaStreamProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Conflict with Manual Beans: Remove the @EnableKafkaStreams annotation
 * <p>
 * When you manually define beans for StreamsBuilder and StreamsConfig,
 * it can lead to conflicts if @EnableKafkaStreams is also present. This is because Spring Boot tries to
 * auto-configure these beans, leading to potential duplication or misconfiguration
 */
@Configuration
@Slf4j
@Profile("words-stream")
public class WordProcessorStreamConfiguration {

    private final static String WORDS_TOPIC = "words-stream";
    private final static String WORDS_COUNT_TOPIC = "words-count";

    @Autowired
    private KafkaStreamProperties kafkaStreamProperties;

    @Bean
    public Map<String, Object> configMap() {
        Map<String, Object> props = kafkaStreamProperties.toMap();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-streaming-appication");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
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
        KStream<String, String> wordsKStream = streamsBuilder()
                .stream("greetings", Consumed.with(Serdes.String(),Serdes.String())
                     .withOffsetResetPolicy(Topology.AutoOffsetReset.EARLIEST));

        wordsKStream.print(Printed.<String, String>toSysOut().withName("Debugging"));

        wordsKStream.mapValues(value -> value.toString().toUpperCase())
                .flatMapValues(word -> List.of(word.split("\\W+")))
                .foreach((key, value) -> log.info("The key {} , the value {} ", key, value));

//        KTable<String, Long> wordsCount = wordsKStream.mapValues((k, v) -> v.toLowerCase())
//                .flatMapValues((k, v) -> Arrays.asList(v.split("\\W+")))
//                .groupBy((k, v) -> v, Grouped.with(Serdes.String(), Serdes.String()))
//                .count();
//
//        wordsCount.toStream().to(WORDS_COUNT_TOPIC);
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
