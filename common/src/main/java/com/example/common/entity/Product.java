package com.example.common.entity;

import com.example.common.ProductDetail;
import com.example.common.entity.Base.BaseEntity;

import com.example.common.enums.Direction;
import com.example.common.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    private Long id;
    private String name;
    private Integer price;
    private String description;
    //    private ProductStatus status;
//    @ManyToOne
//    @JsonIgnore
//    private Category category;
//    private Double area;
//    private Integer bedroom;
//    private Direction direction;
//    private Integer facade;
    private Integer quantity;
    private Integer capacity;
    private Long ownerId;
    private ProductDetail productDetail;
    //    @ManyToOne
//    @JoinColumn(name = "hotelId")
//    private Hotel hotel;
    private List<Option> option;
    private Integer dateRefund;
    private double scale;

}
