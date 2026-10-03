package com.epam.products.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Slf4j
public class AppConfig {

    // Topic name is externalized so it stays in sync with the producer in ProductServiceImpl.
    @Value("${product.kafka.topic.product-created}")
    private String productCreatedTopicName;

    @Bean
    public NewTopic newTopic() {
        NewTopic newTopic = TopicBuilder.name(productCreatedTopicName)
                .partitions(3)
                .build();
        log.info("created topic: {}", newTopic.name());
        return newTopic;
    }
}
