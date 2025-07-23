package com.dialogdata.main.service;

import com.dialogdata.main.entity.Review;
import com.dialogdata.main.mapper.ReviewMapper;
import com.dialogdata.main.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public Review createReview(Review review) {

        boolean isVerified = reviewRepository.verifyReview(review.getProduct().getId(), review.getUser().getId());
        review.setVerified(isVerified);
        review.setCreatedDate(LocalDate.now());

        return reviewRepository.save(review);
    }

    public Review findById(Integer id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public boolean deleteReview(Integer id) {

        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public Page<Review> findReviewsByProductId(Integer productId, Pageable pageable) {
        return reviewRepository.findAllByProductId(productId, pageable);
    }

    public boolean existsByProductIdAndUserId(Integer productId, Integer userId) {
        return reviewRepository.existsByProductIdAndUserId(productId, userId);
    }

    public BigDecimal getAverageRatingByProductId(Integer id) {
        return reviewRepository.getAverageRatingByProductId(id);
    }
}
