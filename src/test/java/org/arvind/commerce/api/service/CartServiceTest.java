package org.arvind.commerce.api.service;

import org.arvind.commerce.api.dto.ItemDTO;
import org.arvind.commerce.api.model.Cart;
import org.arvind.commerce.api.model.Product;
import org.arvind.commerce.api.repo.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    CartRepository cartRepository;
    @Mock
    ProductService productService;
    @InjectMocks
    CartService cartService;


    //@Test
    void testAddToCart() {

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setQuantity(100);
        itemDTO.setProductId(1L);

        Product product = new Product();
        product.setQuantity(200);
        product.setName("Apple");
        product.setId(1L);
        product.setPrice(10.1);

        when(productService.getProductById(itemDTO.getProductId())).thenReturn(product);
        doNothing().when(cartRepository).save(any(Cart.class));
        doNothing().when(productService).saveProducts(any());
        Cart cart = cartService.addToCart(1L, itemDTO);
        assertEquals(itemDTO.getQuantity() * product.getPrice(), cart.getPrice());

    }


}
