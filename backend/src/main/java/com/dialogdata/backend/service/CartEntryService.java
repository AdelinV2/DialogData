package com.dialogdata.backend.service;

import com.dialogdata.backend.entity.CartEntry;
import com.dialogdata.backend.repository.CartEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartEntryService {

    private final CartEntryRepository cartEntryRepository;

    public void create(CartEntry cartEntry) {
        cartEntryRepository.save(cartEntry);
    }

    public CartEntry findById(Integer id) {
        return cartEntryRepository.findById(id).orElse(null);
    }

    public CartEntry update(Integer id, CartEntry cartEntry) {

        CartEntry existingCartEntry = cartEntryRepository.findById(id).orElse(null);

        if (existingCartEntry == null) {
            return null;
        }

        cartEntry.setId(id);

        return cartEntryRepository.save(cartEntry);
    }

    public boolean deleteById(Integer id) {

        CartEntry cartEntry = cartEntryRepository.findById(id).orElse(null);

        if (cartEntry == null) {
            return false;
        }

        cartEntryRepository.delete(cartEntry);

        return true;
    }

    public CartEntry findByProductIdAndCartId(Integer productId, Integer id) {
        return cartEntryRepository.findByProductIdAndCartId(productId, id);
    }

    public boolean delete(Integer id) {

        CartEntry cartEntry = cartEntryRepository.findById(id).orElse(null);

        if (cartEntry == null) {
            return false;
        }

        cartEntryRepository.delete(cartEntry);

        return true;
    }
}
