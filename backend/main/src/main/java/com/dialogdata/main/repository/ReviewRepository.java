package com.dialogdata.main.repository;

import com.dialogdata.main.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ReviewRepository extends JpaRepository<Review, Integer> {


    Page<Review> findAllByProductId(Integer productId, Pageable pageable);

    boolean existsByProductIdAndUserId(Integer productId, Integer userId);

    @Query("SELECT CASE WHEN COUNT(ce) > 0 THEN true ELSE false END " +
            "FROM CartEntry ce " +
            "WHERE ce.product.id = :productId " +
            "AND ce.cart.user.id = :userId " +
            "AND ce.cart.active = false")
    boolean verifyReview(@Param("productId") Integer productId, @Param("userId") Integer userId);

    @Query("SELECT AVG(r.rating) " +
            "FROM Review r " +
            "WHERE r.product.id = :id")
    BigDecimal getAverageRatingByProductId(@Param("id") Integer id);
}
