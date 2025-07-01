package com.dialogdata.backend.repository;

import com.dialogdata.backend.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

}
