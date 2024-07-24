package com.example.productService.entity;


import com.example.productService.entity.Base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
//    private ProductStatus status;
    @ManyToOne
    @JsonIgnore
    private Category category;
//    private Double area;
//    private Integer bedroom;
//    private Direction direction;
//    private Integer facade;
    private Integer quantity;
    private Integer capacity;
    private Long ownerId;
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private ProductDetail productDetail;
    @ManyToOne
    @JoinColumn(name = "hotelId")
    private Hotel hotel;
    @OneToMany(mappedBy = "product")
    private List<OptionProduct> optionProduct;
    private Integer dateRefund;
    private double scale;
}
