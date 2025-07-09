package com.dialogdata.imageservice.service;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.BaseFile;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.GetFileListRequest;
import io.imagekit.sdk.models.results.Result;
import io.imagekit.sdk.models.results.ResultList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageKitService {

    private final ImageKit imageKit;

    public Result uploadProductImage(String base64, String fileName) throws Exception {

        FileCreateRequest request = new FileCreateRequest(base64, fileName);
        request.setFolder("/products/");

        return imageKit.upload(request);
    }

    public List<String> getProductImages(String productId) throws Exception {

        String prefix = "/products/" + productId + "_";
        GetFileListRequest request = new GetFileListRequest();
        request.setSearchQuery("name^=\"" + prefix + "\"");
        ResultList resultList = imageKit.getFileList(request);

        return resultList.getResults().stream()
                .map(BaseFile::getUrl)
                .toList();
    }
}
