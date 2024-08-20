package org.arvind.commerce.api.controller;

import org.arvind.commerce.api.model.Product;
import org.arvind.commerce.api.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public List<Product> getAllProducts() {

        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getAllProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public Product getProductByName(@RequestParam String name) {
        return productService.getProductByName(name);
    }
}
