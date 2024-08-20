package org.arvind.commerce.api.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.arvind.commerce.api.dto.ItemDTO;
import org.arvind.commerce.api.exception.OutOfStockException;
import org.arvind.commerce.api.exception.ResourceNotFoundException;
import org.arvind.commerce.api.model.Cart;
import org.arvind.commerce.api.model.Item;
import org.arvind.commerce.api.model.Product;
import org.arvind.commerce.api.repo.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public Cart createACart() {
        return cartRepository.save(new Cart());
    }

    public Cart getCartWithReference(String ref) {
        return cartRepository.getCartByRef(ref);
    }

    public Cart getCartById(Long id) {
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if(optionalCart.isEmpty()) {
            log.error("Cart with id {} not found", id);
            throw new ResourceNotFoundException(String.format("Cart with identifier %1$s not found", id));
        }

        return optionalCart.get();
    }

    @Transactional
    public Cart addToCart(Long cartId, ItemDTO item) {
        Item it = new Item();
        Product product = productService.getProductById(item.getProductId());
        if(product == null) {
            log.error("Product id {} does not exist", item.getProductId());
            throw new ResourceNotFoundException(String.format("Product id %1$s does not exist", item.getProductId()));
        }
        if(item.getQuantity() > product.getQuantity()) {
            log.error("{} does not have sufficient quantity. Availability is {}", product.getName(), product.getQuantity());
            throw new OutOfStockException(String.format("Product  %1$s does not have sufficient quantity. Availability is %2$s", product.getName(), product.getQuantity()));
        }
        it.setQuantity(item.getQuantity());
        it.setName(product.getName());
        it.setProductId(item.getProductId());
        Cart cart = getCartById(cartId);
        cart.getItems().add(it);
        cart.setPrice(cart.getPrice() + product.getPrice() * item.getQuantity());
        product.setQuantity(product.getQuantity() - item.getQuantity());
        cart = cartRepository.save(cart);
        productService.saveProducts(List.of(product));
        log.debug("A new Item {} has been added to the cart id {}", it.getName(), cartId);
        return cart;
    }

    public List<Cart> getAllItems() {
        return cartRepository.findAll();
    }

    public void removeItem(Long id) {
        cartRepository.deleteById(id);
        log.info("A cart with id {} has been deleted", id);
    }
}
