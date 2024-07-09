package com.example.productService.dto;

import com.example.productService.entity.Category;
import com.example.productService.enums.Direction;
import com.example.productService.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private ProductStatus status;
    private Integer categoryId;
    private Double area;
    private Integer bedroom;
    private Direction direction;
    private Integer facade;
    private Integer quantity;
}
