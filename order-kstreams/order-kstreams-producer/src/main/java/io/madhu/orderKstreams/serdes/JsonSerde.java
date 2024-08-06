/**
 * Author: Madhu
 * User:madhu
 * Date:22/7/24
 * Time:9:57 PM
 * Project: order-kstreams-producer
 */

package io.madhu.orderKstreams.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerde<T> implements Serde<T> {

    private ObjectMapper objectMapper;

    public JsonSerde(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
    }

    @Override
    public Serializer<T> serializer() {
        return new JsonSerializer(this.objectMapper);
    }

    @Override
    public Deserializer<T> deserializer() {
        return new JsonDeserializer(this.objectMapper);
    }
}
