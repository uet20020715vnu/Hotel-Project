package com.example.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option {

    private Long id;
    private String option;
//    @ManyToOne
//    @JoinColumn(name = "hotelId")
//    private Hotel hotel;
    private Product product;
    private Long price;
    private double scale;
}
