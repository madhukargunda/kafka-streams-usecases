/**
 * Author: Madhu
 * User:madhu
 * Date:28/7/24
 * Time:5:33 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.config;

import io.madhu.toyataSalesAnalytics.constants.CarSalesConstants;
import io.madhu.toyataSalesAnalytics.constants.CarSalesStreamProperties;
import io.madhu.toyataSalesAnalytics.dto.StateCountDTO;
import io.madhu.toyataSalesAnalytics.model.CarSaleInvoice;
import io.madhu.toyataSalesAnalytics.serde.CarSalesInvoiceSerde;
import io.madhu.toyataSalesAnalytics.serde.factory.SerdesFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.Objects;


/**
 * Summary of prerequisites for Creating a Topology
 *
 *
 *
 *
 *
 */
@Configuration
@Slf4j
@Profile("v2")
public class ToyataSalesKStreamConfigurationv2 {

    @Autowired
    private CarSalesStreamProperties carSalesStreamProperties;

    @Bean
    public StreamsBuilder toyotaSalesStreamBuilder() {
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        //Define a state store
      //  StoreBuilder<KeyValueStore<String, Long>> storeBuilder = Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore("CARSALES"), Serdes.String(), Serdes.Long());
      //  streamsBuilder.addStateStore(storeBuilder);
        return streamsBuilder;
    }

    @Bean
    public Map<String, Object> toyataKafkaStreamsConfig() {
        Map<String, Object> config = carSalesStreamProperties.toMap();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, carSalesStreamProperties.getApplicationId());
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, carSalesStreamProperties.getBootstrapServers());
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new CarSalesInvoiceSerde().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SerdesFactory.carSaleInvoiceSerde().getClass().getName());
        config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "1");
        config.put(StreamsConfig.STATE_DIR_CONFIG, carSalesStreamProperties.getStateDir());
        //Fix for :Error message was: org.apache.kafka.common.errors.InvalidReplicationFactorException: Replication factor must be larger than 0.
        config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, "1");
        config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG,carSalesStreamProperties.getCommitInterval());
        return config;
    }

    @Bean
    public StreamsConfig toyataSalesStreamsConfig() {
        return new StreamsConfig(toyataKafkaStreamsConfig());
    }

    @Bean
    public Topology toyotaSalesTopology() {
        StreamsBuilder toyotaSalesStreamBuilder = toyotaSalesStreamBuilder();

        //1.Create the KStream from TOYOTA_CAR_SALES Topic
        KStream<String, CarSaleInvoice> carsSalesKStream = toyotaSalesStreamBuilder.stream(CarSalesConstants.TOYOTA_CAR_SALES,
                Consumed.with(Serdes.String(), new CarSalesInvoiceSerde()).withOffsetResetPolicy(Topology.AutoOffsetReset.EARLIEST));

        //2. Print the KStream from TOYOTA_CAR_SALES with the log label is TOYOTA-CAR-SALES
        carsSalesKStream.print(Printed.<String, CarSaleInvoice>toSysOut().withLabel("TOYOTA-CAR-SALES"));

        //3. Filter the KStream by states Texas.
        carsSalesKStream
                .filter((k, v) -> !Objects.isNull(v.getState()))
                .filter((k, v) -> v.getState().equalsIgnoreCase("Texas"))
                .to(CarSalesConstants.TEXAS_TOYOTA_SALES, Produced.with(Serdes.String(), new CarSalesInvoiceSerde()));

        //4.Subscribe to new Topic
        toyotaSalesStreamBuilder.stream(CarSalesConstants.TEXAS_TOYOTA_SALES, Consumed.with(Serdes.String(), SerdesFactory.carSaleInvoiceSerde()))
                .print(Printed.<String, CarSaleInvoice>toSysOut().withLabel("TEXAS-CAR-SALES"));
        salesCountKTable(carsSalesKStream);
        return toyotaSalesStreamBuilder.build();
    }

    @Bean //Hacking
    public KTable<String,Long> salesCountKTable(KStream<String, CarSaleInvoice> carsSalesKStream) {
        Sinks.Many sink = sink();
        //Count Total Number of State wise car sales count
        KTable<String, Long> carSalesCountByStateWide = carsSalesKStream
                .groupBy((key, value) -> value.getState())
                .count(Named.as(CarSalesConstants.CAR_SALES_STORE_NAME), Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>
                        as(CarSalesConstants.CAR_SALES_STORE_NAME).withCachingDisabled());

        // looping ove the KTable
        carSalesCountByStateWide.toStream().foreach(
                (city, count) -> {
                    sink.tryEmitNext(new StateCountDTO(city, count));
                    log.info(String.format("City :" + city + " has " + count));
                }
        );
        return carSalesCountByStateWide;
    }

    @Bean
    public KafkaStreams toyatoSalesKafkaStreams() {
        KafkaStreams streams = new KafkaStreams(toyotaSalesTopology(), toyataSalesStreamsConfig());
        //Uses the isCleanupOnStart property to conditionally clean up state before starting the streams.
        if (carSalesStreamProperties.isCleanUpOnStart()) {
            log.info("Clearing the KTable Store ");
            streams.cleanUp();
        }
        log.info("Starting Toyata Sales Streams start method");
        streams.start();
        return streams;
    }

    @Bean
    public Sinks.Many<StateCountDTO> sink() {
       return Sinks.many().multicast().onBackpressureBuffer();
    }
}
