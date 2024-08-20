package org.arvind.commerce.api.repo;

import org.arvind.commerce.api.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.ref = ?1")
    Cart getCartByRef(String ref);
}
