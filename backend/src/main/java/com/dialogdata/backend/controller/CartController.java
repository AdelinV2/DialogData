package com.dialogdata.backend.controller;

import com.dialogdata.backend.dto.CartDto;
import com.dialogdata.backend.dto.CartEntryDto;
import com.dialogdata.backend.entity.Cart;
import com.dialogdata.backend.entity.CartEntry;
import com.dialogdata.backend.mapper.CartEntryMapper;
import com.dialogdata.backend.mapper.CartMapper;
import com.dialogdata.backend.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;
    private final CartEntryMapper cartEntryMapper;

    @Operation(summary = "Get cart by current user ID")
    @ApiResponse(responseCode = "200", description = "Cart found")
    @ApiResponse(responseCode = "404", description = "Cart not found")
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByCurrentUserId(@Parameter(description = "ID of the current user", required = true)
                                                          @PathVariable("userId") Integer userId) {

        Cart cart = cartService.findByUserId(userId);

        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @Operation(summary = "Add product to cart")
    @ApiResponse(responseCode = "200", description = "Product added to cart")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @PostMapping("/add/{userId}")
    public ResponseEntity<CartEntryDto> addProductToCart(@Parameter(description = "Product to add to cart", required = true)
                                                         @RequestBody @Valid CartEntryDto cartEntryDto,
                                                         @Parameter(description = "ID of the user", required = true)
                                                         @PathVariable("userId") Integer userId) {

        CartEntry cartEntry = cartService.addProductToCart(cartEntryMapper.toEntity(cartEntryDto), userId);

        if (cartEntry == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(cartEntryMapper.toDto(cartEntry));
    }

    @Operation(summary = "Remove product from cart")
    @ApiResponse(responseCode = "200", description = "Product removed from cart")
    @ApiResponse(responseCode = "404", description = "Product not found in cart")
    @PostMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeProductFromCart(@Parameter(description = "ID of the product to remove", required = true)
                                                      @PathVariable("productId")
                                                      Integer productId,
                                                      @Parameter(description = "ID of) the user", required = true)
                                                      @RequestBody Integer userId) {

        Cart cart = cartService.removeProductFromCart(productId, userId);

        return null;
    }

    @Operation(summary = "Update product quantity in cart")
    @ApiResponse(responseCode = "200", description = "Product quantity updated in cart")
    @ApiResponse(responseCode = "404", description = "Product not found in cart")
    @PutMapping("/update/{productId}")
    public ResponseEntity<Cart> updateProductQuantityInCart(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable("productId") Integer productId,
            @Parameter(description = "New quantity for the product", required = true)
            @RequestParam("quantity") Integer quantity,
            @Parameter(description = "ID of the user", required = true)
            @RequestBody Integer userId) {

        Cart cart = cartService.updateProductQuantityInCart(userId, productId, quantity);

        return null;
    }
}
