package com.dialogdata.backend.service;

import com.dialogdata.backend.dto.CartDto;
import com.dialogdata.backend.dto.CartEntryDto;
import com.dialogdata.backend.entity.Cart;
import com.dialogdata.backend.entity.CartEntry;
import com.dialogdata.backend.entity.Product;
import com.dialogdata.backend.entity.User;
import com.dialogdata.backend.mapper.CartEntryMapper;
import com.dialogdata.backend.mapper.CartMapper;
import com.dialogdata.backend.repository.CartRepository;
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

    public CartDto findCartDtoByUserId(Integer userId) {

        Cart cart = findByUserId(userId);

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

        Cart cart = findByUserId(userId);

        if (cart == null) {
            return null;
        }

        Product product = productService.findProductById(cartEntryDto.getProductId());

        List<CartEntry> existingEntries = cartEntryService.findAllByCartId(cart.getId());

        Optional<CartEntry> existingEntry = existingEntries.stream()
                .filter(entry -> entry.getProduct().getId().equals(cartEntryDto.getProductId()))
                .findFirst();

        CartEntry cartEntry;

        if (existingEntry.isEmpty()) {
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

        cart.setTotalPrice(cart.getTotalPrice().add(cartEntryDto.getPricePerPiece().multiply(BigDecimal.valueOf(cartEntryDto.getQuantity()))));
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
