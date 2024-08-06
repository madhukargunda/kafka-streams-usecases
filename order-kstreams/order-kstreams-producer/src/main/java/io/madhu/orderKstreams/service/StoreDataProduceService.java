/**
 * Author: Madhu
 * User:madhu
 * Date:4/8/24
 * Time:10:49 AM
 * Project: order-kstreams-producer
 */

package io.madhu.orderKstreams.service;

import io.madhu.orderKstreams.model.Address;
import io.madhu.orderKstreams.model.Store;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class StoreDataProduceService {

    private static final String STORES_TOPIC = "stores";

    @Autowired
    private KafkaTemplate<String, List<Store>> kafkaTemplate;

    public List<Store> buildStores() {
        log.info("BuildStore");
        var address1 = new Address("1234 Street 1 ", "", "City1", "State1", "12345");
        var store1 = new Store("store_1234",
                address1,
                "1234567890"
        );

        var address2 = new Address("1234 Street 2 ", "", "City2", "State2", "541321");
        var store2 = new Store("store_4567",
                address2,
                "0987654321"
        );
        var stores = List.of(store1, store2);
        return stores;
    }

    @PostConstruct
    public void dispatch() {
        IntStream.range(0, 1)
                .forEach(o -> {
                    List<Store> stores = this.buildStores();
                    log.info(String.format("Bulk Orders iteration %s - Published ==> %s", stores,stores.size()));
                    kafkaTemplate.send(STORES_TOPIC, this.buildStores());
                });
        kafkaTemplate.send(STORES_TOPIC, this.buildStores());
    }
}
