package com.epam.products.service;

import com.epam.products.model.Product;
import com.epam.products.model.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Publishes a {@link ProductCreatedEvent} to Kafka whenever a new product is created.
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Value("${product.kafka.topic.product-created}")
    private String productCreatedTopicName;

    public ProductServiceImpl(ObjectMapper objectMapper, KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(Product product) {
        ProductCreatedEvent productCreatedEvent = objectMapper.convertValue(product, ProductCreatedEvent.class);
        productCreatedEvent.setProductId(UUID.randomUUID().toString());

        // Publish asynchronously; the product id is used as the record key for partitioning.
        CompletableFuture<SendResult<String, ProductCreatedEvent>> kafkaResult =
                kafkaTemplate.send(productCreatedTopicName, productCreatedEvent.getProductId(), productCreatedEvent);
        kafkaResult.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to send message: {}", exception.getMessage());
            } else {
                log.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });

        return productCreatedEvent.getProductId();
    }
}
