/**
 * Author: Madhu
 * User:madhu
 * Date:21/7/24
 * Time:4:59 PM
 * Project: order-kstreams-producer
 */

package io.madhu.orderKstreams.service;


import io.madhu.orderKstreams.model.Order;
import io.madhu.orderKstreams.model.OrderLineItem;
import io.madhu.orderKstreams.model.OrderType;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

@Service
@Slf4j
public class OrderDataProduceService {

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    private static final String TOPIC_NAME = "orders";

    private AtomicBoolean stopper = new AtomicBoolean(Boolean.FALSE);

    public List<Order> buildOrders() {
        var orderItems = List.of(new OrderLineItem("Bananas", 2, new BigDecimal("2.00")), new OrderLineItem("Iphone Charger", 1, new BigDecimal("25.00")));

        var orderItemsRestaurant = List.of(new OrderLineItem("Pizza", 2, new BigDecimal("12.00")), new OrderLineItem("Coffee", 1, new BigDecimal("3.00")));

        var order1 = new Order(12345, "store_1234", new BigDecimal("27.00"), OrderType.GENERAL, orderItems, LocalDateTime.now());

        var order2 = new Order(54321, "store_1234", new BigDecimal("15.00"), OrderType.RESTAURANT, orderItemsRestaurant, LocalDateTime.now());

        var order3 = new Order(12345, "store_4567", new BigDecimal("27.00"), OrderType.GENERAL, orderItems, LocalDateTime.now());

        var order4 = new Order(12345, "store_4567", new BigDecimal("27.00"), OrderType.RESTAURANT, orderItems, LocalDateTime.now());

        return List.of(order1, order2, order3, order4);
    }

    @PostConstruct
    void produceOrders() {
        IntStream.range(0, 10)
                .forEach(o -> {
                    List<Order> orders = this.buildOrders();
                    for (Order order : orders) {
                        log.info(String.format("Bulk Orders iteration - Published ==> %s", order));
                        kafkaTemplate.send(TOPIC_NAME, order);
                    }
                });
    }

    @PreDestroy
    public void shutdown() {
        stopper.set(Boolean.TRUE);
    }
}
