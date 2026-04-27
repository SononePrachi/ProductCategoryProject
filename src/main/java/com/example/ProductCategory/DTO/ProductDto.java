package com.example.ProductCategory.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {
    private Integer productId;
    private String productName;
    private float productPrice;

    private Integer categoryId;
    private String categoryName;

    private Integer userId;
    private String username;
}
