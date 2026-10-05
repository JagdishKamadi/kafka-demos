package com.epam.products.config;

import com.epam.events.model.ProductCreatedEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka-config.producer")
@Getter
@Setter
@Slf4j
public class KafkaConfig {

    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private String acks;
    private String deliveryTimeoutMs;
    private String lingerMs;
    private String requestTimeoutMs;
    private String idempotence;
    private String maxInFlightRequestPerConnection;
    @Value("${kafka-config.topic.product-created}")
    private String productCreatedTopicName;

    private Map<String, Object> producerConfigs() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        configMap.put(ProducerConfig.ACKS_CONFIG, acks);
        configMap.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs);
        configMap.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        configMap.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
        configMap.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
        configMap.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestPerConnection);
        configMap.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        return configMap;
    }

    @Bean
    public ProducerFactory<String, ProductCreatedEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate() {
        return new KafkaTemplate<String, ProductCreatedEvent>(producerFactory());
    }

    @Bean
    public NewTopic newTopic() {
        // min.insync.replicas: minimum number of in-sync replica brokers required to acknowledge a write;
        // if fewer brokers than this are available, producers using acks=all will fail to persist data.
        return TopicBuilder.name(productCreatedTopicName)
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
