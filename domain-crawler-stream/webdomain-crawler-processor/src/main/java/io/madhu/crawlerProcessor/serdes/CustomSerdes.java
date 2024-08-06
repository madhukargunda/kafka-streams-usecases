/**
 * Author: Madhu
 * User:madhu
 * Date:31/7/24
 * Time:1:22 PM
 * Project: webdomain-crawler-processor
 */

package io.madhu.crawlerProcessor.serdes;

import io.madhu.crawlerProcessor.model.WebDomain;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class CustomSerdes {

    private CustomSerdes() {
    }

    public static Serde<WebDomain> webDomain() {
        JsonSerializer<WebDomain> serializer = new JsonSerializer<>();
        JsonDeserializer<WebDomain> deserializer = new JsonDeserializer<>(WebDomain.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }


}
