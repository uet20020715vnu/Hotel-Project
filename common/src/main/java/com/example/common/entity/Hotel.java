package com.example.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    private Long id;
    private String name;
    private List<Product> product;
//    @OneToMany(mappedBy = "hotel")
//    private List<Option> option;
    private Long ownerId;
    private String address;
    private String addressDetail;


}
