package com.example.productService.entity;


import com.example.productService.entity.Base.BaseEntity;
import com.example.productService.enums.Direction;
import com.example.productService.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private ProductStatus status;
    @ManyToOne
    @JsonIgnore
    private Category category;
    private Double area;
    private Integer bedroom;
    private Direction direction;
    private Integer facade;
    private Integer quantity;

}
