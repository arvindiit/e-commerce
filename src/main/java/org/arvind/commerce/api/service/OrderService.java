package org.arvind.commerce.api.service;

import lombok.extern.slf4j.Slf4j;
import org.arvind.commerce.api.AssignmentApplication;
import org.arvind.commerce.api.dto.CartOrderDTO;
import org.arvind.commerce.api.dto.OrderEmailDTO;
import org.arvind.commerce.api.exception.ResourceNotFoundException;
import org.arvind.commerce.api.model.*;
import org.arvind.commerce.api.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CartService cartService;
    private final CustomerService customerService;

    public OrderService(OrderRepository orderRepository, ProductService productService, CartService cartService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.cartService = cartService;
        this.customerService = customerService;
    }


    public Long createOrder(CartOrderDTO cartOrderDTO) {
        Cart cart = cartService.getCartById(cartOrderDTO.getCartId());
        Orders orders = new Orders();
        //here login check should be done or advised before creating this customer
        Customer customer = new Customer();
        customer.setName(cartOrderDTO.getName());
        customer.setEmailAddress(customer.getEmailAddress());
        Long id = customerService.createCustomer(customer);
        List<Product> productList = new ArrayList<>();
        orders.setCustomerId(id);
        cart.getItems().forEach(x -> {
            Item item = x.clone();
            item.setId(null);
            item.setOrdered(true);
            item.setCreatedAt(LocalDateTime.now());
            orders.getItems().add(item);
            Product p = productService.getProductById(item.getProductId());
            orders.setPrice( orders.getPrice() + p.getPrice() * item.getQuantity());
        });
        productService.saveProducts(productList);
        orders.setCreatedAt(LocalDateTime.now());
        Long orderId =  orderRepository.save(orders).getId();
        log.info("A new order with id {} has been  created ", id);
        sendMessage(orderId);
        return orderId;
    }

    public Orders getOrderById(Long id) {
        Optional<Orders> optionalOrders = orderRepository.findById(id);
        if(optionalOrders.isEmpty()) {
            log.error("Order with id {} not found", id);
            throw new ResourceNotFoundException(String.format("Order with identifier %1$s not found", id));
        }

        return optionalOrders.get();
    }

    public List<Orders> getAllOrders() {
        List<Orders> orders = orderRepository.findAll();
        if(orders.isEmpty()) {
            log.error("No Order found");
            throw new ResourceNotFoundException("No order found");
        }

        return orders;
    }

    private void sendMessage(Long orderId) {
        Orders orders = getOrderById(orderId);
        OrderEmailDTO orderEmailDTO = new OrderEmailDTO();
        orderEmailDTO.setOrderId(orderId);
        orderEmailDTO.setEmailAddress(customerService.getCustomerById(orders.getCustomerId()).getEmailAddress());
        AssignmentApplication.backupQueue.add(orderEmailDTO);
        log.info("Order with id {}  got pushed to the queue for further processing", orderId);
    }

    public void removeItem(Long id) {
        orderRepository.deleteById(id);
        log.info("Order with id {}  got deleted", id);
    }
}
