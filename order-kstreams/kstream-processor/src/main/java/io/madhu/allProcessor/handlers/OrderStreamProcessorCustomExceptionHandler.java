/**
 * Author: Madhu
 * User:madhu
 * Date:6/8/24
 * Time:12:07 AM
 * Project: kstream-processor
 */

package io.madhu.allProcessor.handlers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;

@Slf4j
public class OrderStreamProcessorCustomExceptionHandler implements StreamsUncaughtExceptionHandler {

    @Override
    public StreamThreadExceptionResponse handle(Throwable throwable) {
        log.error("Exception in Application :{} ",throwable.getMessage(),throwable);
        return StreamThreadExceptionResponse.REPLACE_THREAD;
    }
}
