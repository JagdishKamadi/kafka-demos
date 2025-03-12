package com.kafka.consumer_demo.config;

import com.kafka.consumer_demo.model.Location;
import com.kafka.consumer_demo.util.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
public class KafkaConfig {

    private static final Logger LOGGER = LogManager.getLogger(KafkaConfig.class);

    @KafkaListener(topics = Constant.TOPIC_NAME, groupId = Constant.GROUP_TOPIC_NAME)
    public void showLocation(Location location) {
        LOGGER.info("Current location {}", location);
    }
}
