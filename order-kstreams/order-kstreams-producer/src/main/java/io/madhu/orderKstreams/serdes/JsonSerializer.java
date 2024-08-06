/**
 * Author: Madhu
 * User:madhu
 * Date:22/7/24
 * Time:7:26 PM
 * Project: order-kstreams-producer
 */

package io.madhu.orderKstreams.serdes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class JsonSerializer<T> implements Serializer<T> {

    private T tClass;

    private ObjectMapper objectMapper;

    public JsonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public byte[] serialize(String topicName, T tClass) {
        try {
            return this.objectMapper.writeValueAsBytes(tClass);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException e ", e);
            throw new RuntimeException(e);
        }
    }
}
