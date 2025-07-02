package com.dialogdata.backend.service;

import com.dialogdata.backend.entity.Cart;
import com.dialogdata.backend.entity.CartEntry;
import com.dialogdata.backend.entity.User;
import com.dialogdata.backend.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartEntryService cartEntryService;

    public Cart findById(Integer id) {
        return cartRepository.findById(id).orElse(null);
    }

    public Cart findByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart createEmptyCart(User user) {

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.valueOf(0.0));

        return cartRepository.save(cart);
    }

    public Cart update(Integer id, Cart cart) {

        Cart existingCart = cartRepository.findById(id).orElse(null);

        if (existingCart == null) {
            return null;
        }

        cart.setId(id);

        return cartRepository.save(cart);
    }

    public boolean deleteById(Integer id) {

        Cart cart = cartRepository.findById(id).orElse(null);

        if (cart == null) {
            return false;
        }

        cartRepository.delete(cart);

        return true;
    }

    @Transactional
    public CartEntry addProductToCart(CartEntry cartEntry, Integer userId) {

        Cart cart = findByUserId(userId);

        cartEntry.setCart(cart);
        cartEntryService.create(cartEntry);

        cart.setTotalPrice(cart.getTotalPrice().add(cartEntry.getPricePerPiece().multiply(BigDecimal.valueOf(cartEntry.getQuantity()))));
        cartRepository.save(cart);

        return cartEntry;
    }

    @Transactional
    public Cart removeProductFromCart(Integer productId, Integer userId) {

        Cart cart = findByUserId(userId);

        if (cart == null) {
            return null;
        }

        CartEntry cartEntry = cartEntryService.findByProductIdAndCartId(productId, cart.getId());

        if (cartEntry == null) {
            return null;
        }

        cart.setTotalPrice(cart.getTotalPrice().subtract(cartEntry.getPricePerPiece().multiply(BigDecimal.valueOf(cartEntry.getQuantity()))));
        cartEntryService.delete(cartEntry.getId());

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateProductQuantityInCart(Integer userId, Integer productId, Integer quantity) {

        Cart cart = findByUserId(userId);

        if (cart == null) {
            return null;
        }

        CartEntry cartEntry = cartEntryService.findByProductIdAndCartId(productId, cart.getId());

        if (cartEntry == null) {
            return null;
        }

        BigDecimal oldPrice = cartEntry.getPricePerPiece().multiply(BigDecimal.valueOf(cartEntry.getQuantity()));
        cartEntry.setQuantity(quantity);
        BigDecimal newPrice = cartEntry.getPricePerPiece().multiply(BigDecimal.valueOf(quantity));

        cart.setTotalPrice(cart.getTotalPrice().subtract(oldPrice).add(newPrice));
        cartEntryService.update(cartEntry.getId(), cartEntry);

        return cartRepository.save(cart);
    }
}
