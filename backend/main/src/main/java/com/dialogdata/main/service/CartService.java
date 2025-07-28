package com.dialogdata.main.service;

import com.dialogdata.main.dto.CartDto;
import com.dialogdata.main.dto.CartEntryDto;
import com.dialogdata.main.entity.Cart;
import com.dialogdata.main.entity.CartEntry;
import com.dialogdata.main.entity.Product;
import com.dialogdata.main.entity.User;
import com.dialogdata.main.mapper.CartEntryMapper;
import com.dialogdata.main.mapper.CartMapper;
import com.dialogdata.main.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartEntryService cartEntryService;
    private final ProductService productService;
    private final CartMapper cartMapper;
    private final CartEntryMapper cartEntryMapper;

    public Cart findById(Integer id) {
        return cartRepository.findById(id).orElse(null);
    }

    public Cart findByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart findActiveByUserId(Integer userId) {
        return cartRepository.findByUserIdAndActive(userId, true);
    }

    public CartDto findCartDtoByUserId(Integer userId) {

        Cart cart = cartRepository.findByUserIdAndActive(userId, true);

        if (cart == null) {
            return null;
        }

        List<CartEntryDto> cartEntries = cartEntryMapper.toDtoList(cartEntryService.findAllByCartId(cart.getId()));

        CartDto cartDto = cartMapper.toDto(cart);
        cartDto.setCartEntries(cartEntries);

        return cartDto;
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
    public CartEntry addProductToCart(CartEntryDto cartEntryDto, Integer userId) {

        Cart cart = cartRepository.findByUserIdAndActive(userId, true);

        if (cart == null) {
            return null;
        }

        Product product = productService.findProductById(cartEntryDto.getProduct().getId());

        List<CartEntry> existingEntries = cartEntryService.findAllByCartId(cart.getId());

        Optional<CartEntry> existingEntry = existingEntries.stream()
                .filter(entry -> entry.getProduct().getId().equals(cartEntryDto.getProduct().getId()))
                .findFirst();

        CartEntry cartEntry;

        if (!existingEntry.isPresent()) {
            cartEntry = cartEntryMapper.toEntity(cartEntryDto);
            cartEntry.setCart(cart);
            cartEntry.setProduct(product);
            cartEntry = cartEntryService.create(cartEntry);
        }

        else {
            cartEntry = existingEntry.get();
            cartEntry.setQuantity(cartEntry.getQuantity() + cartEntryDto.getQuantity());
            cartEntry.setPricePerPiece(cartEntryDto.getPricePerPiece());
            cartEntry.setTotalPricePerEntry(cartEntryDto.getPricePerPiece().multiply(BigDecimal.valueOf(cartEntry.getQuantity())));
            cartEntry = cartEntryService.update(cartEntry.getId(), cartEntry);
        }

        if (cartEntry.getQuantity() > product.getAvailableQuantity()) {
            throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
        }

        cart.setTotalPrice(cart.getTotalPrice().add(cartEntryDto.getPricePerPiece().multiply(BigDecimal.valueOf(cartEntryDto.getQuantity()))));
        cartRepository.save(cart);

        return cartEntry;
    }

    @Transactional
    public Cart removeProductFromCart(Integer productId, Integer userId) {

        Cart cart = this.findActiveByUserId(userId);

        if (cart == null) {
            return null;
        }

        CartEntry cartEntry = cartEntryService.findByProductIdAndCartId(productId, cart.getId());

        if (cartEntry == null) {
            return null;
        }

        cart.getCartEntries().remove(cartEntry);
        cart.setTotalPrice(cart.getTotalPrice().subtract(cartEntry.getPricePerPiece().multiply(BigDecimal.valueOf(cartEntry.getQuantity()))));
        cartEntryService.delete(cartEntry.getId());

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateProductQuantityInCart(Integer userId, Integer productId, Integer quantity) {

        Cart cart = findActiveByUserId(userId);

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
