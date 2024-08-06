/**
 * Author: Madhu
 * User:madhu
 * Date:28/7/24
 * Time:6:34 PM
 * Project: kstream-toyota-sales-analytics
 */

package io.madhu.toyataSalesAnalytics.serde;

import io.madhu.toyataSalesAnalytics.model.CarSaleInvoice;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CarSalesInvoiceSerde extends Serdes.WrapperSerde<CarSaleInvoice> {

    public CarSalesInvoiceSerde() {
       super(new JsonSerializer<CarSaleInvoice>(), new JsonDeserializer<>(CarSaleInvoice.class));
    }
}
