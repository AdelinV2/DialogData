package com.dialogdata.main.controller;

import com.dialogdata.main.dto.ReviewDto;
import com.dialogdata.main.entity.Review;
import com.dialogdata.main.mapper.ReviewMapper;
import com.dialogdata.main.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    public final ReviewService reviewService;
    public final ReviewMapper reviewMapper;

    @Operation(summary = "Create a new review")
    @ApiResponse(responseCode = "201", description = "Review created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid review data")
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Parameter(description = "Review to create", required = true)
                                                  @RequestBody ReviewDto reviewDto) {

        Review review = reviewService.createReview(reviewMapper.toEntity(reviewDto));

        if (review == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(201).body(reviewMapper.toDto(review));
    }

    @Operation(summary = "Get all reviews of a product")
    @ApiResponse(responseCode = "200", description = "List of reviews retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Product not found or no reviews available")
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<ReviewDto>> getReviewsByProductId(@Parameter(description = "ID of the product", required = true)
                                                           @PathVariable("productId") Integer productId,
                                                           @Parameter(description = "Pagination information", required = false)
                                                           Pageable pageable) {

        Page<Review> reviews = reviewService.findReviewsByProductId(productId, pageable);

        if (reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Page<ReviewDto> reviewDtos = reviews.map(reviewMapper::toDto);

        return ResponseEntity.ok(reviewDtos);
    }

    @Operation(summary = "Check if a user has reviewed a product")
    @ApiResponse(responseCode = "200", description = "Review existence checked successfully")
    @GetMapping("/check/{productId}/{userId}")
    public ResponseEntity<Boolean> checkReviewExists(@Parameter(description = "ID of the product", required = true)
                                                     @PathVariable("productId") Integer productId,
                                                     @Parameter(description = "ID of the user", required = true)
                                                     @PathVariable("userId") Integer userId) {

        boolean exists = reviewService.existsByProductIdAndUserId(productId, userId);

        return ResponseEntity.ok(exists);
    }
}
