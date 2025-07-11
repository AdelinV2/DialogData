package com.dialogdata.main.repository;

import com.dialogdata.main.entity.CartEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartEntryRepository extends JpaRepository<CartEntry, Integer> {

    CartEntry findByProductIdAndCartId(Integer productId, Integer id);

    List<CartEntry> findAllByCartId(Integer id);
}
