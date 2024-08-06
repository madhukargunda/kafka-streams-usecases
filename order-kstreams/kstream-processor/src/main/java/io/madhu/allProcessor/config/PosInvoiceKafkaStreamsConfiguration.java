/**
 * Author: Madhu
 * User:madhu
 * Date:14/7/24
 * Time:11:48 PM
 * Project: order-kstream-processor
 */

package io.madhu.allProcessor.config;

import io.madhu.allProcessor.constants.KafkaStreamProperties;
import io.madhu.allProcessor.handlers.OrderStreamDeserializationExceptionHandler;
import io.madhu.allProcessor.model.posinvoice.PosInvoice;
import io.madhu.allProcessor.serdes.factory.SerdesFactory;
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

import java.util.Map;

@Configuration
@Profile("posinvoice-stream")
@Slf4j
public class PosInvoiceKafkaStreamsConfiguration {

    private static final String POS_INVOICE_TOPIC = "pos-invoice";

    @Autowired
    KafkaStreamProperties kafkaStreamProperties;

    @Bean
    public StreamsConfig posStreamsConfig() {
        return new StreamsConfig(posinvoiceKafkaConfigMap());
    }

    @Bean
    public Map<String, Object> posinvoiceKafkaConfigMap() {
        Map<String, Object> props = kafkaStreamProperties.toMap();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "pos-invoice-stream-app");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SerdesFactory.posInvoiceSerde().getClass().getName());
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, OrderStreamDeserializationExceptionHandler.class.getName());
        //props.put(StreamsConfig.)
        return props;
    }

    @Bean
    public StreamsBuilder posInvoiceStreamBuilder() {
        return new StreamsBuilder();
    }

    @Bean
    public Topology posTopology() {
        StreamsBuilder posStreamBuilder = posInvoiceStreamBuilder();
        KStream<String, PosInvoice> posInvoiceKStream = posStreamBuilder
                .stream(POS_INVOICE_TOPIC, Consumed.with(Serdes.String(),
                                SerdesFactory.posInvoiceSerde())
                        .withName("source-processor")
                        .withOffsetResetPolicy(Topology.AutoOffsetReset.EARLIEST));
        posInvoiceKStream.print(Printed.<String,PosInvoice>toSysOut().withLabel("pos-invoices"));
        return posStreamBuilder.build();
    }


    @Bean
    public KafkaStreams ordersKafkaStreams() {
        KafkaStreams streams = new KafkaStreams(posTopology(), posStreamsConfig());
        //streams.setUncaughtExceptionHandler(new OrderStreamProcessorCustomExceptionHandler());
        //Uses the isCleanupOnStart property to conditionally clean up state before starting the streams.
//        if (kafkaStreamProperties.isCleanUpOnStart()) {
//            streams.cleanUp();
//        }
        log.info("Starting posInvoice Streams start method");
        streams.start();
        return streams;
    }
}
