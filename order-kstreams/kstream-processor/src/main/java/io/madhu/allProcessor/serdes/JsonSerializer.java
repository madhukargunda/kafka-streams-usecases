/**
 * Author: Madhu
 * User:madhu
 * Date:14/7/24
 * Time:5:19 PM
 * Project: order-kstream-processor
 */

package io.madhu.allProcessor.serdes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;


@Slf4j
public class JsonSerializer<T> implements Serializer<T> {

    ObjectMapper objectMapper ;
    private Class<T> tClass;

    public JsonSerializer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    @SneakyThrows
    public byte[] serialize(String topicName, T t) {
        return objectMapper.writeValueAsBytes(t);
    }
}
