package com.example.ProductCategory.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private Integer uid;
    private String username;
    private List<String> role;
}
