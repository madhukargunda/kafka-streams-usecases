/**
 * Author: Madhu
 * User:madhu
 * Date:5/8/24
 * Time:10:42 PM
 * Project: kstream-processor
 */

package io.madhu.allProcessor.handlers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.errors.DeserializationExceptionHandler;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.util.Map;

@Slf4j
public class OrderStreamDeserializationExceptionHandler implements DeserializationExceptionHandler {

    int errCounter = 0;

    @Override
    public DeserializationHandlerResponse handle(ProcessorContext processorContext, ConsumerRecord<byte[], byte[]> consumerRecord, Exception e) {
        log.info("Exception is : {} and the kafka record is: {} ", e.getMessage(), consumerRecord, e);
        return DeserializationHandlerResponse.CONTINUE;
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
