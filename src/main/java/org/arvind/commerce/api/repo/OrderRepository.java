package org.arvind.commerce.api.repo;

import org.arvind.commerce.api.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {

}
