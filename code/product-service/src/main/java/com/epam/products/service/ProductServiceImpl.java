package com.epam.products.service;

import com.epam.products.model.Product;
import com.epam.products.model.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(ObjectMapper objectMapper, KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(Product product) {
        ProductCreatedEvent productCreatedEvent = objectMapper.convertValue(product, ProductCreatedEvent.class);
        productCreatedEvent.setProductId(UUID.randomUUID().toString());
        CompletableFuture<SendResult<String, ProductCreatedEvent>> kafkaResult = kafkaTemplate.send("product-created-events-topic", productCreatedEvent.getProductId(), productCreatedEvent);
        kafkaResult.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to send message: {}", exception.getMessage());
            } else {
                log.info("Message sent successfully: {}", result.getRecordMetadata());
            }
        });
        kafkaResult.join(); // blocking call,making to get response


        return productCreatedEvent.getProductId();
    }
}
