/**
 * Author: Madhu
 * User:madhu
 * Date:9/9/24
 * Time:1:15 AM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.service;

import io.madhu.toyataSalesAnalytics.constants.CarSalesConstants;
import io.madhu.toyataSalesAnalytics.dto.StateCountDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CarSalesAnalysisReactiveService {

    @Autowired
    KafkaStreams toyatoSalesKafkaStreams;

    @Autowired
    KTable<String,Long> salesCountKTable;

    public void getKTableStoreData() {
        ReadOnlyKeyValueStore<String, Long> store = toyatoSalesKafkaStreams
                .store(StoreQueryParameters
                        .fromNameAndType(CarSalesConstants.CAR_SALES_STORE_NAME, QueryableStoreTypes.keyValueStore()));
        store.all()
                .forEachRemaining(stringLongKeyValue ->
                        log.info(String.format("The key %s ,value %s", stringLongKeyValue.key, stringLongKeyValue.value)));
    }

    /**
     * On Demand fetch the records in
     * Reactive way
     *
     * @return
     */
    public Flux<StateCountDTO> getAllStateStoreRecords() {
        ReadOnlyKeyValueStore<String, Long> store = toyatoSalesKafkaStreams
                .store(StoreQueryParameters.fromNameAndType(CarSalesConstants.CAR_SALES_STORE_NAME, QueryableStoreTypes.keyValueStore()));
        return Flux.create(sink -> {
            store.all().forEachRemaining(stringLongKeyValue -> {
                sink.next(new StateCountDTO(stringLongKeyValue.key, stringLongKeyValue.value));
            });
            sink.complete();
        });
    }

    /**
     * Continuously fetch the records and send to
     * the dashboard in reactive way
     * @return
     *
     * Here we can handle the InvalidStateStoreException by adding the try catch block and
     * we can customize with our own exception
     */
    public Flux<StateCountDTO> getStreamOfStateRecords() {
        return Flux.interval(Duration.ofSeconds(5)).flatMap(tick -> Flux.fromIterable(fetchStateCount()).distinct());
       // return Flux.interval(Duration.ofSeconds(5)).flatMap(tick -> getKTableCDCUpdates());
    }

    /**
     * C
     * @return
     */
    public Flux<StateCountDTO> getKTableCDCUpdates(){
        log.info("Get KTableCDC Updates method invoked");
        salesCountKTable.toStream().foreach((city, count) -> log.info(String.format("In Service City :" + city + " has " + count)));
        return Flux.create(sink -> {
           salesCountKTable.toStream()
                   .peek((stateName,count) -> log.info("From peek method key: {} value: {}",stateName,count))
                   .foreach((stateName,count) -> {
                       log.info("The Key {} and value {}",stateName,count);
               sink.next(new StateCountDTO(stateName,count));
           });
          sink.complete();
       });
    }

    /**
     * Reusable function to fetch the record
     * @return
     */
    private List<StateCountDTO> fetchStateCount() {
        ArrayList arrayList = new ArrayList();
        ReadOnlyKeyValueStore<String, Long> store = toyatoSalesKafkaStreams
                 .store(StoreQueryParameters
                .fromNameAndType(CarSalesConstants.CAR_SALES_STORE_NAME, QueryableStoreTypes.keyValueStore()));
        store.all().forEachRemaining(stringLongKeyValue -> {
            arrayList.add(new StateCountDTO(stringLongKeyValue.key, stringLongKeyValue.value));
        });
        return arrayList;
    }
}
