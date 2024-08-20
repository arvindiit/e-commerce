package org.arvind.commerce.api.controller;

import org.arvind.commerce.api.dto.CartOrderDTO;
import org.arvind.commerce.api.model.Orders;
import org.arvind.commerce.api.service.CartService;
import org.arvind.commerce.api.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public Orders getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/all")
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Long createOrder(@RequestBody CartOrderDTO cartOrderDTO) {
        Long orderId = orderService.createOrder(cartOrderDTO);
        cartService.removeItem(cartOrderDTO.getCartId());
        return orderId;
    }

    @DeleteMapping("/remove/{id}")
    public void removeItem(@PathVariable Long id) {
        orderService.removeItem(id);
    }
}
