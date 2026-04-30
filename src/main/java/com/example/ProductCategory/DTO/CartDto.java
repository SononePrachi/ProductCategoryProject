package com.example.ProductCategory.DTO;



import lombok.Data;

@Data
public class CartDto {

    private int id;

    private int userId;

    private int productId;

    private int quantity;
}
