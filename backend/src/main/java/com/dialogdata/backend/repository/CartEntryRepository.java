package com.dialogdata.backend.repository;

import com.dialogdata.backend.entity.CartEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartEntryRepository extends JpaRepository<CartEntry, Integer> {
}
