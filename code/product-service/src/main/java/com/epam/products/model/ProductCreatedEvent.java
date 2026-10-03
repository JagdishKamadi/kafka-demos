package com.epam.products.model;

import lombok.*;

import java.math.BigDecimal;

/**
 * Kafka event payload published after a product has been created.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductCreatedEvent {
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
