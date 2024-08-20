package org.arvind.commerce.api.repo;

import org.arvind.commerce.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where lower(p.name) like lower(concat(?1,'%'))")
    Product getProductByName(String name);
}
