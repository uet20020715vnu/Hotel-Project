package com.example.productService.dto;

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
    private Integer categoryId;
    private Integer capacity;
    private Integer quantity;
    private Long ownerId;
    private Integer dateRefund;
    private double scale;
    private Long hotelId;
}
