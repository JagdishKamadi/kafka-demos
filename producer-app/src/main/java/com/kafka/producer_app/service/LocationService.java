package com.kafka.producer_app.service;

import com.kafka.producer_app.model.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.kafka.producer_app.utils.Constant.TOPIC_NAME;

@Service
public class LocationService {

    private static final Logger LOGGER = LogManager.getLogger(LocationService.class);


    private final KafkaTemplate<String, Location> kafkaTemplate;

    public LocationService(KafkaTemplate<String, Location> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Location getLocation() {
        Location location = new Location(String.valueOf(Math.round(Math.random() * 100)), String.valueOf(Math.round(Math.random() * 100)));

        LOGGER.info("Updated location {}", location);
        kafkaTemplate.send(TOPIC_NAME, location);
        return location;
    }
}
