package org.arvind.commerce.api.service;

import lombok.extern.slf4j.Slf4j;
import org.arvind.commerce.api.exception.ResourceNotFoundException;
import org.arvind.commerce.api.model.Product;
import org.arvind.commerce.api.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> optional = productRepository.findById(id);
        if(optional.isEmpty()) {
            log.error("Product with id {} not found", id);
            throw new ResourceNotFoundException(String.format("Product with identifier %1$s not found", id));
        }
        return optional.get();
    }

    public Product getProductByName(String name) {
        return productRepository.getProductByName(name);
    }

    public void saveProducts(List<Product> products) {
        productRepository.saveAll(products);
    }
}
