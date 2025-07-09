package com.dialogdata.main.client;

import com.dialogdata.main.dto.ImageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "image-service", url = "${IMAGE_SERVICE_URL:http://localhost:8081/api/image}")
public interface ImageClient {

    @PostMapping
    void uploadImage(ImageDto imageDto);

    @GetMapping("/get/{productId}")
    List<String> getProductImagesUrl(@PathVariable("productId") Integer productId);
}
