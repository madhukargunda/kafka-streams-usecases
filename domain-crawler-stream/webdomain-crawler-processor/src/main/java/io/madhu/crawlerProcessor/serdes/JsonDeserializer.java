/**
 * Author: Madhu
 * User:madhu
 * Date:31/7/24
 * Time:1:18 PM
 * Project: webdomain-crawler-processor
 */

package io.madhu.crawlerProcessor.serdes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JsonDeserializer<T> implements Deserializer<T> {

    private final Gson gson = new GsonBuilder().create();

    private Class<T> destinationClass;
    private Type reflectionTypeToken;

    public JsonDeserializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }

    public JsonDeserializer(Type reflectionTypeToken) {
        this.reflectionTypeToken = reflectionTypeToken;
    }

    @Override
    public T deserialize(String topic, byte[] bytes) {
        if (bytes == null)
            return null;
        try {
            Type type = destinationClass != null ? destinationClass : reflectionTypeToken;
            return gson.fromJson(new String(bytes, StandardCharsets.UTF_8), type);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing message", e);
        }
    }

}
