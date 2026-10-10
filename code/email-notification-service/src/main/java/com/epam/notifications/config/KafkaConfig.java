package com.epam.notifications.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka-config.consumer")
@Getter
@Setter
@Slf4j
public class KafkaConfig {

    private String bootstrapServers;
    private String keyDeserializer;
    private String errorValueDeserializer;
    private String valueDeserializer;
    private String groupId;
    private String trustedPackages;

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, errorValueDeserializer);
        configMap.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, valueDeserializer);
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configMap.put("spring.json.trusted.packages", trustedPackages);
        return configMap;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(KafkaTemplate<String, Object> kafkaTemplate) {
        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate));
        ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        kafkaListenerContainerFactory.setCommonErrorHandler(defaultErrorHandler);
        return kafkaListenerContainerFactory;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configMap);
    }
}
