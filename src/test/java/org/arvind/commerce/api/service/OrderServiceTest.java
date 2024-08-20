package org.arvind.commerce.api.service;

import org.arvind.commerce.api.AssignmentApplication;
import org.arvind.commerce.api.dto.CartOrderDTO;
import org.arvind.commerce.api.model.*;
import org.arvind.commerce.api.repo.OrderRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    ProductService productService;
    @Mock
    CartService cartService;
    @Mock
    CustomerService customerService;

    @InjectMocks
    OrderService orderService;

    @BeforeAll
    static void setup(){
        AssignmentApplication.backupQueue = new LinkedList<>();
    }

    @Test
    void testCreateOrder() {

        CartOrderDTO cartOrderDTO = new CartOrderDTO();
        cartOrderDTO.setCartId(1L);
        cartOrderDTO.setName("Test");
        cartOrderDTO.setEmailAddress("email");
        Cart cart = createCart();
        when(cartService.getCartById(1L)).thenReturn(cart);
        when(customerService.createCustomer(any())).thenReturn(1L);
        Product p1 = new Product();
        p1.setPrice(10);
        Product p2 = new Product();
        p2.setPrice(10);
        Orders orders = new Orders();
        orders.setId(1L);
        when(productService.getProductById(cart.getItems().get(0).getProductId())).thenReturn(p1);
        when(productService.getProductById(cart.getItems().get(1).getProductId())).thenReturn(p2);
        doNothing().when(productService).saveProducts(any());
        when(orderRepository.save(any())).thenReturn(orders);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(orders));
        Customer customer = new Customer();
        customer.setEmailAddress("Test@Test.com");
        when(customerService.getCustomerById(any())).thenReturn(customer);

        Long id = orderService.createOrder(cartOrderDTO);
        assertEquals(1L, id);

    }

    private Cart createCart() {
        Cart cart = new Cart();
        List<Item> list = new ArrayList<>();

        Item item1 = new Item();
        item1.setName("Apple");
        item1.setQuantity(10);
        item1.setProductId(1L);
        list.add(item1);

        Item item2 = new Item();
        item1.setName("Grapes");
        item1.setQuantity(20);
        item1.setProductId(2L);
        list.add(item2);

        cart.setItems(list);
        return cart;
    }


}
