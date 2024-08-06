/**
 * Author: Madhu
 * User:madhu
 * Date:14/7/24
 * Time:5:15 PM
 * Project: order-kstream-processor
 */

package io.madhu.orderProcessor.serdes;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.madhu.orderProcessor.model.Order;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerde<T> implements Serde<T> {

    private T object;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Serializer<T> serializer() {
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //this.objectMapper.configure(SerializationFeature.)
//        this.objectMapper.activateDefaultTyping(
//                objectMapper.getPolymorphicTypeValidator(),
//                ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
//                JsonTypeInfo.As.PROPERTY);
//        this.objectMapper.registerModule(new SimpleModule().addAbstractTypeMapping(Order.class, Order.class));
        return new JsonSerializer<T>(this.objectMapper);
    }

    @Override
    public Deserializer<T> deserializer() {
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
//        this.objectMapper.activateDefaultTyping(
//                objectMapper.getPolymorphicTypeValidator(),
//                ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE,
//                JsonTypeInfo.As.PROPERTY);
//        this.objectMapper.registerModule(new SimpleModule().addAbstractTypeMapping(Order.class, Order.class));

        return new JsonDeserializer<T>(this.objectMapper);
    }
}
