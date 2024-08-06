/**
 * Author: Madhu
 * User:madhu
 * Date:15/9/24
 * Time:7:54 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.exception;


import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     *   In Kafka Streams, if we encounter an InvalidStateStoreException because we are attempting to access the KTable's
     *   state store before the stream thread has fully transitioned to the RUNNING state.
     *   Kafka Streams can be in several states, such as STARTING, RUNNING, REBALANCING, PENDING_SHUTDOWN, etc.
     *   and our stream must be in the RUNNING state before accessing the store.
     *   So here we are checking the KAFKA Streams is ready for access or not.
     */
    @ExceptionHandler(InvalidStateStoreException.class)
    public String handleStreamNotReady(InvalidStateStoreException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "dashboard-ui-error";
    }
}
