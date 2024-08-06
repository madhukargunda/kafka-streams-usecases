/**
 * Author: Madhu
 * User:madhu
 * Date:22/7/24
 * Time:10:09 PM
 * Project: order-kstreams-producer
 */

package io.madhu.orderKstreams.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.madhu.orderKstreams.model.Order;
import io.madhu.orderKstreams.serdes.JsonSerde;
import org.apache.kafka.common.serialization.Serde;


public class SerdeFactory {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Serde<Order> orderSerde(){
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        return new JsonSerde<Order>(objectMapper);
    }
}
