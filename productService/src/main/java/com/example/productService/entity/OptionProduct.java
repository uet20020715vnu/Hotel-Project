package com.example.productService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OptionProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String optionProduct;
//    @ManyToOne
//    @JoinColumn(name = "hotelId")
//    private Hotel hotel;
    @ManyToOne
    @JsonIgnore
    private Product product;
    private Long price;
    private double scale;
}
