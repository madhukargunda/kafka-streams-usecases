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
import io.madhu.allProcessor.model.orders.Order;
import io.madhu.allProcessor.model.orders.OrderType;
import io.madhu.allProcessor.serdes.factory.SerdesFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
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
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Map;

@Configuration
@Profile("orders-stream")
@Slf4j
public class OrderKafkaStreamsConfiguration {

    private static final String ORDERS_TOPIC = "orders";
    private static final String RESTAURANT_ORDERS_TOPIC = "restaurant-orders";
    private static final String GENERAL_ORDERS_TOPIC = "general-orders";

    @Bean
    public NewTopic generalOrders() {
        return TopicBuilder.name(RESTAURANT_ORDERS_TOPIC).partitions(1).build();
    }

    @Bean
    public NewTopic restaurantOrders() {
        return TopicBuilder.name(GENERAL_ORDERS_TOPIC).partitions(1).build();
    }


    @Autowired
    KafkaStreamProperties kafkaStreamProperties;

    @Bean
    public StreamsConfig ordersStreamsConfig() {
        return new StreamsConfig(ordersKafkaConfigMap());
    }

    @Bean
    public Map<String, Object> ordersKafkaConfigMap() {
        Map<String, Object> props = kafkaStreamProperties.toMap();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-orders-stream-app");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        JsonSerde<Order> orderJsonSerde = new JsonSerde<>();
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SerdesFactory.orderSerde().getClass().getName());
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);
        //LogAndContinueExceptionHandler or LogAndFailExceptionHandler
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, OrderStreamDeserializationExceptionHandler.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "io.madhu.orderProcessor.model.orders");
        return props;
    }

    @Bean
    public StreamsBuilder ordersStreamBuilder() {
        return new StreamsBuilder();
    }

    @Bean
    public Topology ordersTopology() {
        StreamsBuilder orderStreamBuilder = ordersStreamBuilder();

        KStream<String, Order> orderStreams = orderStreamBuilder.stream(ORDERS_TOPIC, Consumed.with(Serdes.String(), SerdesFactory.orderSerde()).withName("source-processor").withOffsetResetPolicy(Topology.AutoOffsetReset.EARLIEST));
        orderStreams.print(Printed.<String, Order>toSysOut().withLabel("Orders-"));
        //Requirement 1 - Split the orders based on their type (Restaurant or General) and send them to the corresponding Kafka topics

        // Define predicates for splitting
        Predicate<String, Order> generalOrdersPredicate = (key, order) -> order.orderType().name().equals(OrderType.GENERAL.name());
        Predicate<String, Order> resturantOrdersPredicate = (key, order) -> order.orderType().name().equals(OrderType.RESTAURANT.name());

        // Split the stream based on the predicates
        //extracted(orderStreams, generalOrdersPredicate, restaurantOrdersPredicate);
        Map<String, KStream<String, Order>> branches = orderStreams.split(Named.as("split-stream-")).branch((key, order) -> order.orderType().name().equals(OrderType.GENERAL.name()), Branched.as("general-orders")).branch((key, order) -> order.orderType().name().equals(OrderType.RESTAURANT.name()), Branched.as("restaurant-orders")).noDefaultBranch();

        // Process general orders
        branches.get("split-stream-general-orders").print(Printed.<String, Order>toSysOut().withLabel("general-orders"));
        branches.get("split-stream-restaurant-orders").print(Printed.<String, Order>toSysOut().withLabel("restaurant-orders"));

//        if (!Objects.isNull(generalOrdersStream)) {
//            generalOrdersStream.print(Printed.<String, Order>toSysOut().withLabel("general-order-stream"));
//            generalOrdersStream.to(GENERAL_ORDERS_TOPIC, Produced.with(Serdes.String(), SerdesFactory.orderSerde()));
//        }

        // Process restaurant orders
//        KStream<String, Order> restaurantOrdersStream = branches.get("restaurant-orders");
//        if (!Objects.isNull(restaurantOrdersStream)) {
//            restaurantOrdersStream.print(Printed.<String, Order>toSysOut().withLabel("restaurant-order-stream"));
//            restaurantOrdersStream.to(RESTAURANT_ORDERS_TOPIC, Produced.with(Serdes.String(), SerdesFactory.orderSerde()));
//        }

        return orderStreamBuilder.build();
    }

    private static void extracted(KStream<String, Order> orderStreams, Predicate<String, Order> generalOrdersPredicate, Predicate<String, Order> resturantOrdersPredicate) {
        orderStreams.split(Named.as("split-stream-")).branch(generalOrdersPredicate, Branched.withConsumer(generalOrdersStream -> {
            generalOrdersStream.print(Printed.<String, Order>toSysOut().withLabel("general-order-stream"));
            generalOrdersStream.to(GENERAL_ORDERS_TOPIC, Produced.with(Serdes.String(), SerdesFactory.orderSerde()));
        })).branch(resturantOrdersPredicate, Branched.withConsumer(restaurantOrdersStream -> {
            restaurantOrdersStream.print(Printed.<String, Order>toSysOut().withLabel("general-order-stream"));
            restaurantOrdersStream.to(RESTAURANT_ORDERS_TOPIC, Produced.with(Serdes.String(), SerdesFactory.orderSerde()));
        })).noDefaultBranch();
    }

    @Bean
    public KafkaStreams ordersKafkaStreams() {
        KafkaStreams streams = new KafkaStreams(ordersTopology(), ordersStreamsConfig());
        //streams.setUncaughtExceptionHandler(new OrderStreamProcessorCustomExceptionHandler());
        //Uses the isCleanupOnStart property to conditionally clean up state before starting the streams.
        if (kafkaStreamProperties.isCleanUpOnStart()) {
            streams.cleanUp();
        }
        log.info("Starting OrdersKafkaStrem Streams start method");
        streams.start();
        return streams;
    }
}
