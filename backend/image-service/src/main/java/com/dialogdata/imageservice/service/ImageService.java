package com.dialogdata.imageservice.service;

import com.dialogdata.imageservice.entity.ImageGetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageKitService imageKitService;

    public void uploadProductImage(ImageGetDto imageGetDto) throws Exception {
        imageKitService.uploadProductImage(imageGetDto.getBase64(), imageGetDto.getFileName());
    }

    public List<String> getProductImages(Integer productId) throws Exception {

        return imageKitService.getProductImages(productId);
    }

    public void deleteProductImages(Integer productId) throws Exception {
        imageKitService.deleteProductImages(productId);
    }
}
