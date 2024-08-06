/**
 * Author: Madhu
 * User:madhu
 * Date:9/9/24
 * Time:12:22 AM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
public class DisplayDashboardController {

    @Autowired
    private KafkaStreams kafkaStreams;

    @GetMapping("/dashboard")
    public String displayDashBoard(Model model) {
//        if (isKTableIsReady(model)) {
//            return "dashboard-ui";
//        } else
//            return "dashboard-ui-error";
        return "dashboard-ui";
    }

    /**
     *   In Kafka Streams, if we encounter an InvalidStateStoreException because we are attempting to access the KTable's
     *   state store before the stream thread has fully transitioned to the RUNNING state.
     *   Kafka Streams can be in several states, such as STARTING, RUNNING, REBALANCING, PENDING_SHUTDOWN, etc.
     *   and our stream must be in the RUNNING state before accessing the store.
     *   So here we are checking the KAFKA Streams is ready for access or not.
     */
    private boolean isKTableIsReady(Model model){
        if(kafkaStreams.state() == KafkaStreams.State.RUNNING){
            log.info("KafkaStream Thread is not ready");
            model.addAttribute("errorMessage",kafkaStreams.state().toString());
            return true;
        } else
            return false;
    }
}
