/**
 * Author: Madhu
 * User:madhu
 * Date:14/7/24
 * Time:11:25 PM
 * Project: order-kstream-processor
 */

package io.madhu.allProcessor.serdes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Deserializer;

public class JsonDeserializer<T> implements Deserializer<T> {

    ObjectMapper objectMapper ;
    private Class<T> tclass;

    public JsonDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false);
    }

    @Override
    @SneakyThrows
    public T deserialize(String s, byte[] bytes) {
        return objectMapper.readValue(bytes, new TypeReference<T>() { });
    }
}
