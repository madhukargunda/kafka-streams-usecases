/**
 * Author: Madhu
 * User:madhu
 * Date:14/7/24
 * Time:11:52 PM
 * Project: order-kstream-processor
 */

package io.madhu.allProcessor.serdes.factory;

import io.madhu.allProcessor.model.orders.Order;
import io.madhu.allProcessor.model.posinvoice.PosInvoice;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


public class SerdesFactory {

    static public Serde<Order> orderSerde() {
        org.springframework.kafka.support.serializer.JsonSerde<Order> orderJsonSerde = new org.springframework.kafka.support.serializer.JsonSerde<>();
        Map<String, Object> serdeProps = getOrderSerdeJsonConfigMap(Order.class.getName());
        orderJsonSerde.configure(serdeProps, false); //
        return orderJsonSerde;
    }

    private static Map<String, Object> getOrderSerdeJsonConfigMap(String Name) {
        Map<String, Object> serdeProps = new HashMap<>();
        serdeProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        serdeProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Name);
        serdeProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        serdeProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return serdeProps;
    }

    static public Serde<PosInvoice> posInvoiceSerde() {
        org.springframework.kafka.support.serializer.JsonSerde<PosInvoice> posInvoiceJsonSerde = new org.springframework.kafka.support.serializer.JsonSerde<>();
        // Custom settings
        Map<String, Object> serdeProps = getOrderSerdeJsonConfigMap(PosInvoice.class.getName());
        posInvoiceJsonSerde.configure(serdeProps, false); //
        return posInvoiceJsonSerde;
    }
}
