package com.epam.notifications.handler;

import com.epam.events.model.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "product-created-events-topic")
@Slf4j
public class ProductCreatedEventHandler {

    @KafkaHandler
    public void Handle(ProductCreatedEvent productCreatedEvent) {
        log.info("Received events {}", productCreatedEvent.getTitle());
    }
}
