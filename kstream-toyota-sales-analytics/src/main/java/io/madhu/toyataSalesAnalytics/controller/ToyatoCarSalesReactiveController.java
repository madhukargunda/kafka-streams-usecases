/**
 * Author: Madhu
 * User:madhu
 * Date:9/9/24
 * Time:12:21 AM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.controller;

import io.madhu.toyataSalesAnalytics.dto.StateCountDTO;
import io.madhu.toyataSalesAnalytics.service.CarSalesAnalysisReactiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
public class ToyatoCarSalesReactiveController {

    @Autowired
    private CarSalesAnalysisReactiveService carSalesAnalysisReactiveService;

    @Autowired
    private Sinks.Many sink;

    @GetMapping(value = "/reactive/car/sales", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StateCountDTO> streamStateCounts() {
        return carSalesAnalysisReactiveService.getAllStateStoreRecords();
    }

  //  @GetMapping(value = "/reactive/stream/car/sales", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StateCountDTO> streamOfSaleCounts() {
        return carSalesAnalysisReactiveService.getStreamOfStateRecords();
    }

    //private Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

    @GetMapping(value = "/reactive/stream/car/sales", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getStateUpdates() {
        return sink.asFlux();
    }
}
