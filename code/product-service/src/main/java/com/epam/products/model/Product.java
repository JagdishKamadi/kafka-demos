package com.epam.products.model;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Product {
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
