/**
 * Author: Madhu
 * User:madhu
 * Date:14/7/24
 * Time:5:16 PM
 * Project: order-kstream-processor
 */

package io.madhu.allProcessor.config;

import io.madhu.allProcessor.constants.KafkaStreamProperties;
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

import java.util.Map;


/**
 * 1.Create the StreamsConfig object
 * 2.Create the Topology object by calling the StreamBuilder.build() method
 * 3.Create the
 */
@Configuration
@Slf4j
@Profile("greetings-stream")
public class GreetingKafkaStreamsConfiguration {

    private final static String GREETING_TOPIC="greetings";

    private final static String GREETING_UPPERCASE_TOPIC ="greeting-upper";

    @Autowired
    private KafkaStreamProperties kafkaStreamProperties;

    public Map<String, Object> configMap() {
        Map<String, Object> props = kafkaStreamProperties.toMap();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-greetings-stream-app" + "-");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);
        return props;
    }

    @Bean
    public StreamsConfig greetingsStreamsConfig() {
        return new StreamsConfig(configMap());
    }

    @Bean
    public StreamsBuilder greetingsStreamsBuilder() {
        return new StreamsBuilder();
    }

    @Bean
    public Topology greetingsTopology() {
        KStream<String, String> greetingsKstream = greetingsStreamsBuilder()
                .stream(GREETING_TOPIC, Consumed.with(Serdes.String(),Serdes.String())
                        .withOffsetResetPolicy(Topology.AutoOffsetReset.EARLIEST));
        greetingsKstream.print(Printed.<String,String>toSysOut().withName("uppercase"));
        greetingsKstream.mapValues(value -> value.toString().toUpperCase()).to(GREETING_UPPERCASE_TOPIC);
        return greetingsStreamsBuilder().build();
    }

    @Bean
    public KafkaStreams greetingKafkaStreams() {
        KafkaStreams streams = new KafkaStreams(greetingsTopology(), greetingsStreamsConfig());
        // Uses the isCleanupOnStart property to conditionally clean up state before starting the streams.
        if(kafkaStreamProperties.isCleanUpOnStart()){
            streams.cleanUp();
        }
        streams.start();
        return streams;
    }
}
