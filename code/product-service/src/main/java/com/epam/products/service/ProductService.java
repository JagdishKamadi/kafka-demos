package com.epam.products.service;

import com.epam.products.model.Product;

/**
 * Handles product creation and downstream event publishing.
 */
public interface ProductService {

    /**
     * Creates a product and publishes a corresponding creation event.
     *
     * @param product the product data submitted by the caller
     * @return the generated unique id of the created product
     */
    String createProduct(Product product);
}
