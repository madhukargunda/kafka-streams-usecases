/**
 * Author: Madhu
 * User:madhu
 * Date:22/7/24
 * Time:9:44 PM
 * Project: order-kstreams-producer
 */

package io.madhu.orderKstreams.serdes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
public class JsonDeserializer<T> implements Deserializer<T> {

    private Class<T> classT;

    private ObjectMapper objectMapper;

    public JsonDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
    }

    @Override
    public T deserialize(String s, byte[] data) {
        log.info("Deserializing the object .......");
        try {
            return this.objectMapper.readValue(data, classT);
        } catch (IOException e) {
            log.error("IOException {} ", e);
            throw new RuntimeException(e);
        }
    }
}
