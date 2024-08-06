/**
 * Author: Madhu
 * User:madhu
 * Date:28/7/24
 * Time:5:33 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.config;

import io.madhu.toyataSalesAnalytics.constants.KafkaStreamProperties;
import io.madhu.toyataSalesAnalytics.model.CarSaleInvoice;
import io.madhu.toyataSalesAnalytics.serde.CarSalesInvoiceSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;

@Configuration
@Slf4j
public class ToyataSalesKStreamConfiguration {

    private static final String TOYATO_CAR_SALES_TOPIC_NAME = "toyata-car-sales";

    private static final String TEXAS_TOYATO_CAR_SALES_TOPIC_NAME = "texas-toyata-car-sales";

    @Autowired
    private KafkaStreamProperties kafkaStreamProperties;

    @Bean
    public StreamsBuilder toyatoSalesStreamBuilder() {
        return new StreamsBuilder();
    }

    @Bean
    public Map<String, Object> toyatoKafkaStreamsConfig() {
        Map<String, Object> config = kafkaStreamProperties.toMap();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "kstream-toyota-sales-analytics");
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaStreamProperties.getApplicationId() + "-");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new CarSalesInvoiceSerde().getClass().getName());
        config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);
        return config;
    }

    @Bean
    public StreamsConfig toyatoSalesStreamsConfig() {
        return new StreamsConfig(toyatoKafkaStreamsConfig());
    }

    @Bean
    public Topology toyatoSalesTopology() {
        StreamsBuilder carsSalesStream = toyatoSalesStreamBuilder();
        KStream<String,CarSaleInvoice> carSaleInvoiceKStream = carsSalesStream.stream(TOYATO_CAR_SALES_TOPIC_NAME,
                Consumed.with(Serdes.String(), new CarSalesInvoiceSerde()));
        //carSaleInvoiceKStream.print(Printed.<String, CarSaleInvoice>toSysOut().withLabel("Cars sales"));
        KStream<String,CarSaleInvoice> texasCarSalesStream = carSaleInvoiceKStream.filter((k,v) -> !Objects.isNull(v.getState()))
                .filter((k,v) -> v.getState().equalsIgnoreCase("Texas"));

        texasCarSalesStream.to(TEXAS_TOYATO_CAR_SALES_TOPIC_NAME,Produced.with(Serdes.String(), new CarSalesInvoiceSerde()));
        texasCarSalesStream.print(Printed.<String, CarSaleInvoice>toSysOut().withLabel("Texas Cars sales"));

        return carsSalesStream.build();
    }

    @Bean
    public KafkaStreams toyatoSalesKafkaStreams() {
        KafkaStreams streams = new KafkaStreams(toyatoSalesTopology(), toyatoSalesStreamsConfig());
        //Uses the isCleanupOnStart property to conditionally clean up state before starting the streams.
        if (kafkaStreamProperties.isCleanUpOnStart()) {
            streams.cleanUp();
        }
        log.info("Starting Toyata Sales Streams start method");
        streams.start();
        return streams;
    }
}
