package com.dialogdata.backend.repository;

import com.dialogdata.backend.entity.CartEntryList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartEntryListRepository extends JpaRepository<CartEntryList, Integer> {
}
