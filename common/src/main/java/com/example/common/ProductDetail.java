package com.example.common;

import com.example.common.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {
    private long productId;
    private Product product;
    private Integer area;
    private String convenient;
    private String bathroom;
    private String view;
    private String utility;
    private Boolean smoke;
}
