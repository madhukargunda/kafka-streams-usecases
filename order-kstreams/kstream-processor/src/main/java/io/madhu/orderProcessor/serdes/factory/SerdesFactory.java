/**
 * Author: Madhu
 * User:madhu
 * Date:14/7/24
 * Time:11:52 PM
 * Project: order-kstream-processor
 */

package io.madhu.orderProcessor.serdes.factory;

import io.madhu.orderProcessor.model.Order;
import io.madhu.orderProcessor.serdes.JsonSerde;
import org.apache.kafka.common.serialization.Serde;

import java.util.List;


public class SerdesFactory {

    static public Serde<Order> orderSerde(){
        return new JsonSerde<Order>();
    }
}
