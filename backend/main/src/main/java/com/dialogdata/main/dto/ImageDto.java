package com.dialogdata.main.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private String imageUrl;
    private String base64;
    private String fileName;
}
