package com.example.productService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductDetail {
    @Id
    private long productId;
    @MapsId
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer area;
    private String convenient;
    private String bathroom;
    private String view;
    private String utility;
    private Boolean smoke;
}
