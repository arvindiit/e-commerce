package org.arvind.commerce.api.controller;

import org.arvind.commerce.api.dto.ItemDTO;
import org.arvind.commerce.api.model.Cart;
import org.arvind.commerce.api.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {


    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/new")
    public Long getACart() {
        return cartService.createACart().getId();
    }

    @GetMapping
    public Cart getCartWithIdentifier(@RequestParam String ref) {
        return cartService.getCartWithReference(ref);
    }

    @GetMapping("/{id}")
    public Cart getCartWithId(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    @PostMapping("/{id}/addItem")
    public Cart addItem(@PathVariable Long id, @RequestBody ItemDTO item) {
        return cartService.addToCart(id, item);
    }

    @DeleteMapping("/remove/{id}")
    public void removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
    }
}
