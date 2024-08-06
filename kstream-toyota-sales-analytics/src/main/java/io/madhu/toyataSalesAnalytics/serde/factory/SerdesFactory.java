/**
 * Author: Madhu
 * User:madhu
 * Date:5/9/24
 * Time:11:31 AM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.serde.factory;

import io.madhu.toyataSalesAnalytics.model.CarSaleInvoice;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

public class SerdesFactory {

    private static Map<String, Object> getCarSaleInvoiceSerdeConfiguration(String name) {
        Map<String, Object> serdeProps = new HashMap<>();
        serdeProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        serdeProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, name);
        serdeProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        serdeProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return serdeProps;
    }

    static public Serde<CarSaleInvoice> carSaleInvoiceSerde() {
        JsonSerde<CarSaleInvoice> posInvoiceJsonSerde = new JsonSerde<>();
        // Custom settings
        Map<String, Object> serdeProps = getCarSaleInvoiceSerdeConfiguration(CarSaleInvoice.class.getName());
        posInvoiceJsonSerde.configure(serdeProps, false); //
        return posInvoiceJsonSerde;
    }
}
