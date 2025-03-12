package com.kafka.consumer_demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.consumer_demo.model.Location;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class LocationDeserializer implements Deserializer<Location> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No special configuration needed
    }

    @Override
    public Location deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            return objectMapper.readValue(data, Location.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing User object", e);
        }
    }

    @Override
    public void close() {
        // Nothing to close
    }
}
