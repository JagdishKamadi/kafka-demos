package com.epam.products.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
public class AppConfig {

    @Bean
    public NewTopic newTopic() {
        NewTopic newTopic = TopicBuilder.name("product-created-events-topic")
                .partitions(3)
                .build();
        log.info("created topic: {}", newTopic.name());
        return newTopic;
    }
}
