package com.epam.products.service;

import com.epam.events.model.ProductCreatedEvent;
import com.epam.products.exception.ProductNotCreatedException;
import com.epam.products.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Value("${kafka-config.topic.product-created}")
    private String productCreatedTopicName;

    public ProductServiceImpl(ObjectMapper objectMapper, KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(Product product) {
        ProductCreatedEvent productCreatedEvent = objectMapper.convertValue(product, ProductCreatedEvent.class);
        productCreatedEvent.setProductId(UUID.randomUUID().toString());

        try {
            // now making this call as synchronous
            SendResult<String, ProductCreatedEvent> result = kafkaTemplate.send(productCreatedTopicName, productCreatedEvent.getProductId(), productCreatedEvent).get();
            log.info("Topic : {}", result.getRecordMetadata().topic());
            log.info("Partition : {}", result.getRecordMetadata().partition());
            log.info("Offset : {}", result.getRecordMetadata().partition());
        } catch (InterruptedException | ExecutionException e) {
            throw new ProductNotCreatedException("Failed to create product");
        }
        return productCreatedEvent.getProductId();
    }
}
