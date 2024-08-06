/**
 * Author: Madhu
 * User:madhu
 * Date:31/7/24
 * Time:1:13 PM
 * Project: webdomain-crawler-processor
 */

package io.madhu.crawlerProcessor.serdes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class JsonSerializer<T> implements Serializer<T> {

    private final Gson gson = new GsonBuilder().create();

    @Override
    public byte[] serialize(String s, T data) {
        if (data == null) return null;

        try {
            return gson.toJson(data).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }
}
