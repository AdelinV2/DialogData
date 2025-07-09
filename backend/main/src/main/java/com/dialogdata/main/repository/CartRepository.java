package com.dialogdata.main.repository;

import com.dialogdata.main.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUserId(Integer userId);

    Cart findByUserIdAndActive(Integer userId, boolean active);
}
