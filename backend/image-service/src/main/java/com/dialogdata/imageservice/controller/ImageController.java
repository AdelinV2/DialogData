package com.dialogdata.imageservice.controller;

import com.dialogdata.imageservice.entity.ImageGetDto;
import com.dialogdata.imageservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<Void> uploadImage(@RequestBody ImageGetDto imageGetDto) throws Exception {

        imageService.uploadProductImage(imageGetDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/get/{productId}")
    public ResponseEntity<List<String>> getProductImages(@PathVariable String productId) throws Exception {

        List<String> images = imageService.getProductImages(productId);

        return ResponseEntity.ok(images);
    }
}
