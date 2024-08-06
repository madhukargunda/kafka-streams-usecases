/**
 * Author: Madhu
 * User:madhu
 * Date:7/9/24
 * Time:11:56 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.service;

import io.madhu.toyataSalesAnalytics.common.HttpRequestAndResponseContextHolder;
import io.madhu.toyataSalesAnalytics.constants.CarSalesConstants;
import io.madhu.toyataSalesAnalytics.constants.CarSalesStreamProperties;
import io.madhu.toyataSalesAnalytics.dto.CarSalesReportDTO;
import io.madhu.toyataSalesAnalytics.dto.StateCountDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CarSalesAnalysisService {

    @Autowired
    KafkaStreams toyotaSalesKafkaStreams;

    @Autowired
    CarSalesStreamProperties carSalesStreamProperties;

    @Autowired
    HttpRequestAndResponseContextHolder httpRequestAndResponseContextHolder;

    public CarSalesReportDTO getSalesCountByStateName(String stateName) {
        return new CarSalesReportDTO(stateName, queryStore(stateName));
    }

    @EventListener(ApplicationReadyEvent.class)
    private void printStateDirectory() {
        log.info("The State Directory value is  :{}", carSalesStreamProperties.getStateDir());
        log.info("The number of threads configured is {}", carSalesStreamProperties.getNumStreamThreads());
        String fullPath = Paths.get(carSalesStreamProperties.getStateDir()).toAbsolutePath().normalize().toString();
        log.info("Full path of Kafka Streams state directory: {}", fullPath);
        log.info("The Application Id {}", carSalesStreamProperties.getApplicationId());
    }

    private Long queryStore(String stateName) {
        // if (toyotaSalesKafkaStreams.state() == KafkaStreams.State.RUNNING) {
        ReadOnlyKeyValueStore<String, Long> store = toyotaSalesKafkaStreams.store(StoreQueryParameters
                .fromNameAndType(CarSalesConstants.CAR_SALES_STORE_NAME, QueryableStoreTypes.keyValueStore()));
        // Query the state store for the specific key
        return store.get(stateName);
//        } else {
//            return String.format("Value for key %s  state %d");
//        }
    }

    public Long getSalesCountByStateNamev2(String stateName) {
        ReadOnlyKeyValueStore<String, Long> keyValueStore = toyotaSalesKafkaStreams.store(StoreQueryParameters
                .fromNameAndType(CarSalesConstants.CAR_SALES_STORE_NAME, QueryableStoreTypes.keyValueStore()));
        return keyValueStore.get(stateName);
    }


    public List<StateCountDTO> getSalesCount() {
        ReadOnlyKeyValueStore<String, Long> keyValueStore = toyotaSalesKafkaStreams.store(StoreQueryParameters
                .fromNameAndType(CarSalesConstants.CAR_SALES_STORE_NAME, QueryableStoreTypes.keyValueStore()));
        ArrayList<StateCountDTO> stateCountDTOArrayList = new ArrayList<>();
        keyValueStore.all().forEachRemaining(entry -> {
            stateCountDTOArrayList.add(new StateCountDTO(entry.key, entry.value));
        });
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return stateCountDTOArrayList;
    }

}
