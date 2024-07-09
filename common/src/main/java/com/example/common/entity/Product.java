package com.example.common.entity;

import com.example.common.entity.Base.BaseEntity;

import com.example.common.enums.Direction;
import com.example.common.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private ProductStatus status;
    private Category category;
    private Double area;
    private Integer bedroom;
    private Direction direction;
    private Integer facade;
    private Integer quantity;

}
