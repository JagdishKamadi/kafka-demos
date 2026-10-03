package com.epam.products.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
@Slf4j
public class AppConfig {

    @Value("${product.kafka.topic.product-created}")
    private String productCreatedTopicName;

    @Bean
    public NewTopic newTopic() {
        // min.insync.replicas: minimum number of in-sync replica brokers required to acknowledge a write;
        // if fewer brokers than this are available, producers using acks=all will fail to persist data.
        NewTopic newTopic = TopicBuilder.name(productCreatedTopicName)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
        return newTopic;
    }
}
