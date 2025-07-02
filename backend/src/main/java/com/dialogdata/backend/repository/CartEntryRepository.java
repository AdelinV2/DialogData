package com.dialogdata.backend.repository;

import com.dialogdata.backend.entity.CartEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartEntryRepository extends JpaRepository<CartEntry, Integer> {

    CartEntry findByProductIdAndCartId(Integer productId, Integer id);

    List<CartEntry> findAllByCartId(Integer id);
}
