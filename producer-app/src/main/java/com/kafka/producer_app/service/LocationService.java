package com.kafka.producer_app.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.kafka.producer_app.utils.Constant.TOPIC_NAME;

@Service
public class LocationService {

    private static final Logger LOGGER = LogManager.getLogger(LocationService.class);


    private final KafkaTemplate<String, String> kafkaTemplate;

    public LocationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String getLocation() {
        String location = null;
        for (int i = 0; i < 1000; i++) {
            location = "(" + Math.round(Math.random() * 100) + " , " + Math.round(Math.random() * 100) + ")";
            LOGGER.info("Updated location {}", location);
            kafkaTemplate.send(TOPIC_NAME, location);
        }
        return location;
    }
}
